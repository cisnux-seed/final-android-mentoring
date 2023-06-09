package dev.cisnux.dicodingmentoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.data.realtime.ChatRealtimeDataSource
import dev.cisnux.dicodingmentoring.data.realtime.ChatRealtimeDataSourceImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ChatRealtimeDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindChatRealtimeDataSource(
        chatRealtimeDataSourceImpl: ChatRealtimeDataSourceImpl
    ): ChatRealtimeDataSource
}