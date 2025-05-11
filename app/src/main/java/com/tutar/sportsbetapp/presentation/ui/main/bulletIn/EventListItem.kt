package com.tutar.sportsbetapp.presentation.ui.main.bulletIn

import com.tutar.sportsbetapp.data.model.OddsResponse

sealed class EventListItem {
    data class LeagueHeader(val leagueName: String, val imageRes: Int? = null) : EventListItem()
    data class EventDetail(val event: OddsResponse) : EventListItem()
}
