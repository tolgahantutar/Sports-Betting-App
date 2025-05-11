package com.tutar.sportsbetapp.data.dataSource

import com.tutar.sportsbetapp.data.model.OddsResponse
import com.tutar.sportsbetapp.data.remote.api.OddsApiService
import javax.inject.Inject

class OddsRemoteDataSource @Inject constructor(
    private val apiService: OddsApiService
) {
    suspend fun getOddsForSport(sport: String): List<OddsResponse> {
        return apiService.getOddsForSport(sport, apiKey = "YOUR_API_KEY")
    }
}