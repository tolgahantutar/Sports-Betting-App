package com.tutar.sportsbetapp.domain.usecase

import com.tutar.sportsbetapp.core.util.Resource
import com.tutar.sportsbetapp.data.model.OddsResponse
import com.tutar.sportsbetapp.domain.repository.OddsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOddsUseCase @Inject constructor(
    private val repository: OddsRepository
) {
    operator fun invoke(): Flow<Resource<List<OddsResponse>>> = flow {
        emit(Resource.Loading)
        try {
            val sports = listOf(
                "soccer_turkey_super_league",
                "soccer_italy_serie_a",
                "soccer_germany_bundesliga",
                "soccer_france_ligue_one"
            )
            val odds = repository.getOddsForSports(sports)
            emit(Resource.Success(odds))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
