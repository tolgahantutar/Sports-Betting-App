package com.tutar.sportsbetapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutar.sportsbetapp.core.util.Resource
import com.tutar.sportsbetapp.data.model.OddsResponse
import com.tutar.sportsbetapp.domain.usecase.GetOddsUseCase
import com.tutar.sportsbetapp.presentation.ui.main.bulletIn.EventListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getOddsUseCase: GetOddsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<EventListItem>>>(Resource.Loading)
    val uiState: StateFlow<Resource<List<EventListItem>>> = _uiState

    fun fetchOdds() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            _uiState.value = try {
                val odds = getOddsUseCase()
                val grouped = groupEventsByLeague(odds)
                Resource.Success(grouped)
            } catch (e: Exception) {
                Resource.Error("Failed to fetch odds: ${e.message}")
            }
        }
    }

    private fun groupEventsByLeague(events: List<OddsResponse>): List<EventListItem> {
        return events.groupBy { it.sportTitle.orEmpty() }
            .flatMap { (league, group) ->
                listOf(EventListItem.LeagueHeader(league)) + group.map { EventListItem.EventDetail(it) }
            }
    }
}