package com.tutar.sportsbetapp.presentation.ui.main.bulletIn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tutar.sportsbetapp.R
import com.tutar.sportsbetapp.data.AnalyticsLogger
import com.tutar.sportsbetapp.data.mappers.formatOddLabel
import com.tutar.sportsbetapp.data.model.OddsResponse
import com.tutar.sportsbetapp.databinding.ItemEventBinding
import com.tutar.sportsbetapp.databinding.ItemEventDetailBinding
import com.tutar.sportsbetapp.databinding.OddsComponentBinding
import com.tutar.sportsbetapp.presentation.ui.main.MainConstants.Companion.VIEW_TYPE_EVENT
import com.tutar.sportsbetapp.presentation.ui.main.MainConstants.Companion.VIEW_TYPE_HEADER
import com.tutar.sportsbetapp.presentation.viewModel.SelectedBet

class EventsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val allItems = mutableListOf<EventListItem>()
    private val displayedItems = mutableListOf<EventListItem>()

    private val selectedBets = mutableListOf<SelectedBet>()

    var onOddSelected: ((event: OddsResponse, eventId: String, isAlreadySelected: Boolean, gameName: String, oddName: String, price: Double, marketKey: String, point: Double?) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (displayedItems[position]) {
            is EventListItem.LeagueHeader -> VIEW_TYPE_HEADER
            is EventListItem.EventDetail -> VIEW_TYPE_EVENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemEventBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemEventDetailBinding.inflate(inflater, parent, false)
                EventViewHolder(binding)
            }
        }
    }


    override fun getItemCount(): Int = displayedItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = displayedItems[position]) {
            is EventListItem.LeagueHeader -> (holder as HeaderViewHolder).bind(item)
            is EventListItem.EventDetail -> (holder as EventViewHolder).bind(item.event)
        }
    }

    fun updateItems(newItems: List<EventListItem>) {
        allItems.clear()
        allItems.addAll(newItems)
        displayedItems.clear()
        displayedItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setSelectedBets(bets: List<SelectedBet>) {
        selectedBets.clear()
        selectedBets.addAll(bets)
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(
        private val binding: ItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EventListItem.LeagueHeader) {
            binding.tvLeagueName.text = item.leagueName
            binding.ivEventImage
                .setImageResource(R.drawable.sports_soccer_24dp)
        }
    }

    inner class EventViewHolder(
        private val binding: ItemEventDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val tvGameName = binding.tvGameName
        private val ivShowOdds = binding.ivShowOdds
        private val oddsContainer = binding.oddsContainer

        private var isExpanded = false

        fun bind(event: OddsResponse) {
            tvGameName.text = "${event.homeTeam} - ${event.awayTeam}"
            oddsContainer.removeAllViews()
            isExpanded = event.isExpanded
            oddsContainer.visibility = if (isExpanded) View.VISIBLE else View.GONE
            ivShowOdds.rotation = if (isExpanded) 180f else 0f

            if (isExpanded) {
                inflateOdds(event, adapterPosition)
                ivShowOdds.setColorFilter(ContextCompat.getColor(ivShowOdds.context, R.color.red))
            } else {
                ivShowOdds.setColorFilter(ContextCompat.getColor(ivShowOdds.context, R.color.greenColor))
            }

            ivShowOdds.setOnClickListener {
                isExpanded = !isExpanded
                event.isExpanded = isExpanded
                if (isExpanded) {
                    oddsContainer.visibility = View.VISIBLE
                    ivShowOdds.animate().rotation(180f).start()
                    ivShowOdds.setColorFilter(ContextCompat.getColor(ivShowOdds.context, R.color.red))
                    inflateOdds(event, adapterPosition)

                    AnalyticsLogger.logEventDetailOpened(itemView.context, "${event.homeTeam} - ${event.awayTeam}")

                } else {
                    oddsContainer.visibility = View.GONE
                    oddsContainer.removeAllViews()
                    ivShowOdds.setColorFilter(ContextCompat.getColor(ivShowOdds.context, R.color.greenColor))
                    ivShowOdds.animate().rotation(0f).start()
                }
            }
        }

        private fun inflateOdds(event: OddsResponse, adapterPosition: Int) {
            val context = binding.root.context

            val selected = selectedBets.firstOrNull { it.eventId == event.id }
            val selectedOddName = selected?.oddName

            val outcomeTriples = event.bookmakers
                ?.firstOrNull()
                ?.markets
                ?.flatMap { market ->
                    market.outComes.orEmpty().map { outcome ->
                        Triple(market.key, outcome, outcome.point)
                    }
                }
                .orEmpty()

            outcomeTriples.forEach { (marketKey, outcome, point) ->
                val oddBinding = OddsComponentBinding.inflate(LayoutInflater.from(context), binding.oddsContainer, false)


                oddBinding.tvOdd.text =
                    outcome.price?.toString() ?: "-"

                val formattedLabel = formatOddLabel(
                    outcome.name,
                    marketKey,
                    point,
                    event.homeTeam,
                    event.awayTeam
                )
                oddBinding.tvOddName.text = formattedLabel

                val isSelected = outcome.name == selectedOddName
                val tvOdd = oddBinding.tvOdd
                tvOdd.setBackgroundResource(
                    if (isSelected) R.color.selectedOddColor else android.R.color.transparent
                )

                oddBinding.root.setOnClickListener {
                    val isNowSelected = event.selectedOddName != outcome.name
                    event.isExpanded = isExpanded
                    event.selectedOddName = if (isNowSelected) outcome.name else null
                    notifyItemChanged(adapterPosition)

                    val eventName = "${event.homeTeam} - ${event.awayTeam}"

                    if (isNowSelected) {
                        AnalyticsLogger.logOddSelected(context, eventName, outcome.name.orEmpty())
                    } else {
                        AnalyticsLogger.logOddDeselected(context, eventName, outcome.name.orEmpty())
                    }

                    onOddSelected?.invoke(
                        event,
                        event.id.orEmpty(),
                        !isNowSelected,
                        "${event.homeTeam} - ${event.awayTeam}",
                        outcome.name.orEmpty(),
                        outcome.price ?: 0.0,
                        marketKey ?: "",
                        outcome.point
                    )
                }

                oddsContainer.addView(oddBinding.root)
            }
        }



    }

    fun filter(query: String) {
        if (query.isBlank()) {
            displayedItems.clear()
            displayedItems.addAll(allItems)
            notifyDataSetChanged()
            return
        }
        val lowerCaseQuery = query.lowercase()

        val filtered = allItems
            .groupBy {
                if (it is EventListItem.EventDetail) it.event.sportTitle.orEmpty() else ""
            }
            .flatMap { (_, groupItems) ->
                val matchingEvents = groupItems.filter {
                    it is EventListItem.EventDetail && (
                            it.event.homeTeam?.contains(lowerCaseQuery, true) == true ||
                                    it.event.awayTeam?.contains(lowerCaseQuery, true) == true
                            )
                }
                if (matchingEvents.isNotEmpty()) {
                    listOf(EventListItem.LeagueHeader((groupItems.firstOrNull() as? EventListItem.EventDetail)?.event?.sportTitle.orEmpty())) +
                            matchingEvents
                } else {
                    emptyList()
                }
            }

        displayedItems.clear()
        displayedItems.addAll(filtered)
        notifyDataSetChanged()
    }
}
