package com.tutar.sportsbetapp.domain.repository

import com.tutar.sportsbetapp.data.model.OddsResponse

interface OddsRepository {
    suspend fun getOddsForSports(sports: List<String>): List<OddsResponse>
}