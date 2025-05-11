package com.tutar.sportsbetapp.di

import com.google.firebase.auth.FirebaseAuth
import com.tutar.sportsbetapp.data.dataSource.AuthRemoteDataSource
import com.tutar.sportsbetapp.data.dataSource.OddsRemoteDataSource
import com.tutar.sportsbetapp.data.remote.api.OddsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideAuthRemoteDataSource(
        firebaseAuth: FirebaseAuth
    ): AuthRemoteDataSource = AuthRemoteDataSource(firebaseAuth)

    @Provides
    fun provideOddsRemoteDataSource(
        oddsApiService: OddsApiService
    ): OddsRemoteDataSource = OddsRemoteDataSource(oddsApiService)
}