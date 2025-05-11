package com.tutar.sportsbetapp.di

import com.tutar.sportsbetapp.domain.repository.AuthRepository
import com.tutar.sportsbetapp.domain.repository.OddsRepository
import com.tutar.sportsbetapp.domain.usecase.AuthUseCase
import com.tutar.sportsbetapp.domain.usecase.GetOddsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideAuthUseCase(
        authRepository: AuthRepository
    ): AuthUseCase = AuthUseCase(
        authRepository
    )

    @Provides
    fun provideGetOddsUseCase(
        oddsRepository: OddsRepository
    ): GetOddsUseCase = GetOddsUseCase(
        oddsRepository
    )
}