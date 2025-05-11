package com.tutar.sportsbetapp.data.repositoryImpl

import com.tutar.sportsbetapp.data.dataSource.AuthRemoteDataSource
import com.tutar.sportsbetapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
): AuthRepository {

    override suspend fun login(email: String, password: String) {
        remoteDataSource.login(email, password)
    }

    override suspend fun signUp(email: String, password: String) {
        remoteDataSource.signUp(email, password)
    }

    override suspend fun signOut() {
        remoteDataSource.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return remoteDataSource.isUserLoggedIn()
    }
}