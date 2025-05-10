package com.tutar.sportsbetapp.di

import com.tutar.sportsbetapp.data.dataSource.AuthRemoteDataSource
import com.tutar.sportsbetapp.data.repositoryImpl.AuthRepositoryImpl
import com.tutar.sportsbetapp.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(
        authRemoteDataSource: AuthRemoteDataSource
    ): AuthRepository = AuthRepositoryImpl(
        authRemoteDataSource
    )
}