package com.tutar.sportsbetapp.domain.usecase

import com.tutar.sportsbetapp.core.util.Resource
import com.tutar.sportsbetapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    fun login(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            repository.login(email, password)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }

    fun signUp(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            repository.signUp(email, password)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }

    fun checkIsUserLoggedIn(): Boolean {
        return repository.isUserLoggedIn()
    }
}