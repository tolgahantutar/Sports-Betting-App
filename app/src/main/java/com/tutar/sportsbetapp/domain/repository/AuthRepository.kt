package com.tutar.sportsbetapp.domain.repository


interface AuthRepository {

    suspend fun login(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun signOut()
    fun isUserLoggedIn(): Boolean
}