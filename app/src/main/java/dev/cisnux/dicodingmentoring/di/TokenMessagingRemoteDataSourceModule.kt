package dev.cisnux.dicodingmentoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.data.remote.TokenMessagingRemoteDataSource
import dev.cisnux.dicodingmentoring.data.remote.TokenMessagingRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenMessagingRemoteDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindTokenMessagingRemoteDataSource(
        tokenMessagingRemoteDataSourceImpl: TokenMessagingRemoteDataSourceImpl
    ): TokenMessagingRemoteDataSource
}