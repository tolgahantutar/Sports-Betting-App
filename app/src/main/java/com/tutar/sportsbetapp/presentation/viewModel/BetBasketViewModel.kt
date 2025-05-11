package com.tutar.sportsbetapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BetBasketViewModel @Inject constructor() : ViewModel() {

    private val _selectedBets = MutableStateFlow<List<SelectedBet>>(emptyList())
    val selectedBets: StateFlow<List<SelectedBet>> = _selectedBets

    fun addOrReplaceBet(bet: SelectedBet) {
        _selectedBets.value = _selectedBets.value
            .filterNot { it.eventId == bet.eventId } + bet
    }

    fun removeBet(eventId: String) {
        _selectedBets.value = _selectedBets.value.filterNot { it.eventId == eventId }
    }

    fun clearBets() {
        _selectedBets.value = emptyList()
    }
}

data class SelectedBet(
    val eventId: String,
    val gameName: String,
    val oddName: String,
    val price: Double,
    val marketKey: String,   // "h2h" or "totals"
    val point: Double?,      // only for totals
    val homeTeam: String?,
    val awayTeam: String?
)
