package com.tutar.sportsbetapp.di

import com.google.firebase.auth.FirebaseAuth
import com.tutar.sportsbetapp.data.dataSource.AuthRemoteDataSource
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
}