package com.tutar.sportsbetapp.presentation.ui.main.bulletIn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tutar.sportsbetapp.R
import com.tutar.sportsbetapp.data.model.OddsResponse
import com.tutar.sportsbetapp.presentation.ui.main.MainConstants.Companion.VIEW_TYPE_EVENT
import com.tutar.sportsbetapp.presentation.ui.main.MainConstants.Companion.VIEW_TYPE_HEADER

class EventsAdapter(
    private val items: MutableList<EventListItem> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is EventListItem.LeagueHeader -> VIEW_TYPE_HEADER
            is EventListItem.EventDetail -> VIEW_TYPE_EVENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = inflater.inflate(R.layout.item_event, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_event_detail, parent, false)
                EventViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is EventListItem.LeagueHeader -> (holder as HeaderViewHolder).bind(item)
            is EventListItem.EventDetail -> (holder as EventViewHolder).bind(item.event)
        }
    }

    fun updateItems(newItems: List<EventListItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: EventListItem.LeagueHeader) {
            itemView.findViewById<TextView>(R.id.tvLeagueName).text = item.leagueName
            item.imageRes?.let {
                itemView.findViewById<ImageView>(R.id.ivEventImage).setImageResource(it)
            }
        }
    }

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvGameName = view.findViewById<TextView>(R.id.tvGameName)
        private val ivShowOdds = view.findViewById<ImageView>(R.id.ivShowOdds)
        private val oddsContainer = view.findViewById<LinearLayout>(R.id.oddsContainer)

        private var isExpanded = false

        fun bind(event: OddsResponse) {
            tvGameName.text = "${event.homeTeam} - ${event.awayTeam}"
            oddsContainer.removeAllViews()
            oddsContainer.visibility = View.GONE
            ivShowOdds.rotation = 0f
            isExpanded = false

            ivShowOdds.setOnClickListener {
                isExpanded = !isExpanded
                if (isExpanded) {
                    oddsContainer.visibility = View.VISIBLE
                    ivShowOdds.animate().rotation(180f).start()
                    inflateOdds(event)
                } else {
                    oddsContainer.visibility = View.GONE
                    ivShowOdds.animate().rotation(0f).start()
                }
            }
        }

        private fun inflateOdds(event: OddsResponse) {
            val context = itemView.context
            val outcomes = event.bookmakers
                ?.firstOrNull()
                ?.markets
                ?.flatMap { it.outComes.orEmpty() }
                .orEmpty()

            outcomes.forEach { outcome ->
                val oddView = LayoutInflater.from(context)
                    .inflate(R.layout.odds_component, oddsContainer, false)

                oddView.findViewById<TextView>(R.id.tvOdd).text = outcome.price?.toString() ?: "-"
                oddView.findViewById<TextView>(R.id.tvOddName).text = outcome.name ?: "-"

                oddsContainer.addView(oddView)
            }
        }
    }
}
