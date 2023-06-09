package dev.cisnux.dicodingmentoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.data.repositories.TokenMessagingRepository
import dev.cisnux.dicodingmentoring.data.repositories.TokenMessagingRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenMessagingRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTokenMessagingRepository(
        tokenMessagingRepositoryImpl: TokenMessagingRepositoryImpl
    ): TokenMessagingRepository
}