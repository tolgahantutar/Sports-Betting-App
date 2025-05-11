package com.tutar.sportsbetapp.presentation.ui.main.basket

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tutar.sportsbetapp.R
import com.tutar.sportsbetapp.data.AnalyticsLogger
import com.tutar.sportsbetapp.data.mappers.formatOddLabel
import com.tutar.sportsbetapp.databinding.ItemBasketBottomSheetBinding
import com.tutar.sportsbetapp.presentation.viewModel.SelectedBet

class BetBasketAdapter : RecyclerView.Adapter<BetBasketAdapter.BetViewHolder>() {

    private val bets = mutableListOf<SelectedBet>()
    var onRemoveClick: ((String) -> Unit)? = null

    fun updateItems(newItems: List<SelectedBet>) {
        bets.clear()
        bets.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BetViewHolder {
        val binding = ItemBasketBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BetViewHolder(binding)
    }

    override fun getItemCount(): Int = bets.size

    override fun onBindViewHolder(holder: BetViewHolder, position: Int) {
        val bet = bets[position]
        holder.binding.tvEventName.text = bet.gameName
        val formattedLabel = formatOddLabel(
            outcomeName = bet.oddName,
            marketKey = bet.marketKey,
            point = bet.point,
            homeTeam = bet.homeTeam,
            awayTeam = bet.awayTeam
        )

        holder.binding.tvOddName.text = formattedLabel
        holder.binding.tvPrice.text = bet.price.toString()

        holder.binding.ivDeleteEvent.setOnClickListener {

            AnalyticsLogger.logOddDeselected(
                holder.itemView.context,
                bet.gameName,
                bet.oddName
            )
            onRemoveClick?.invoke(bet.eventId)
        }
    }

    inner class BetViewHolder(
        val binding: ItemBasketBottomSheetBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bet: SelectedBet) {
            binding.tvEventName.text = bet.gameName
            binding.tvOddName.text = bet.oddName
            binding.tvOddName.text = bet.price.toString()
            binding.ivDeleteEvent.setOnClickListener {
                onRemoveClick?.invoke(bet.eventId)
            }
        }
    }
}
