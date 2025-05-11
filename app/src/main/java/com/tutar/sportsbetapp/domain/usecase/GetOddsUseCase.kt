package com.tutar.sportsbetapp.domain.usecase

import com.tutar.sportsbetapp.data.model.OddsResponse
import com.tutar.sportsbetapp.domain.repository.OddsRepository
import javax.inject.Inject

class GetOddsUseCase @Inject constructor(
    private val repository: OddsRepository
) {
    suspend operator fun invoke(): List<OddsResponse> {
        val sports = listOf(
            "soccer_turkey_super_league",
        )
        return repository.getOddsForSports(sports)
    }
}