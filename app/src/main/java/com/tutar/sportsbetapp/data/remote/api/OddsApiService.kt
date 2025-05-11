package com.tutar.sportsbetapp.data.remote.api

import com.tutar.sportsbetapp.data.model.OddsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OddsApiService {

    @GET("v4/sports/{sport}/odds")
    suspend fun getOddsForSport(
        @Path("sport") sport: String,
        @Query("bookmakers") bookmakers: String = "onexbet",
        @Query("markets") markets: String = "h2h,totals",
        @Query("apiKey") apiKey: String
    ): List<OddsResponse>
}
