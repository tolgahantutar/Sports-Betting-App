package com.tutar.sportsbetapp.data.repositoryImpl

import com.tutar.sportsbetapp.data.dataSource.OddsRemoteDataSource
import com.tutar.sportsbetapp.data.model.OddsResponse
import com.tutar.sportsbetapp.domain.repository.OddsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class OddsRepositoryImpl @Inject constructor(
    private val remoteDataSource: OddsRemoteDataSource
) : OddsRepository {

    override suspend fun getOddsForSports(sports: List<String>): List<OddsResponse> {
        return coroutineScope {
            sports.map { sport ->
                async {
                    try {
                        remoteDataSource.getOddsForSport(sport)
                    } catch (e: Exception) {
                        emptyList<OddsResponse>()
                    }
                }
            }.awaitAll().flatten()
        }
    }
}