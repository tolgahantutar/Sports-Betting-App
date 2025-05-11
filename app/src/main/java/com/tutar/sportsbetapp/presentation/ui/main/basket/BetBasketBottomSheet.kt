package com.tutar.sportsbetapp.presentation.ui.main.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tutar.sportsbetapp.R
import com.tutar.sportsbetapp.databinding.BottomSheetBetBasketBinding
import com.tutar.sportsbetapp.presentation.viewModel.BetBasketViewModel
import com.tutar.sportsbetapp.presentation.viewModel.SelectedBet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@AndroidEntryPoint
class BetBasketBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBetBasketBinding
    private val betBasketViewModel: BetBasketViewModel by activityViewModels()
    private val adapter = BetBasketAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetBetBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = BottomSheetBehavior.from(bottomSheet as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvBetBasket.adapter = adapter
        binding.rvBetBasket.layoutManager = LinearLayoutManager(requireContext())

        binding.customToolbar.inflateMenu(R.menu.menu_bet_basket)

        // Handle close click
        binding.customToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_close -> {
                    dismiss()
                    true
                }
                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            betBasketViewModel.selectedBets.collect { bets ->
                adapter.updateItems(bets)
                updateSummary(bets)
            }
        }

        binding.tieOddPrice.addTextChangedListener {
            updateSummary(betBasketViewModel.selectedBets.value)
        }

        adapter.onRemoveClick = { eventId ->
            betBasketViewModel.removeBet(eventId)
        }
    }

    private fun updateSummary(bets: List<SelectedBet>) {
        val totalOdds = if (bets.isEmpty()) {
            BigDecimal.ZERO
        } else {
            bets.map { it.price }
                .fold(1.0) { acc, odd -> acc * odd }
                .let { BigDecimal(it).setScale(2, RoundingMode.HALF_UP) }
        }

        val amount = binding.tieOddPrice.text.toString().toDoubleOrNull() ?: 0.0
        val maxWin = totalOdds.multiply(BigDecimal.valueOf(amount))
            .setScale(2, RoundingMode.HALF_UP)

        binding.tvTotalOddsValue.text = totalOdds.toPlainString()
        binding.tvMaxWinValue.text = maxWin.toPlainString()
    }

}
