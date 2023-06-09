package dev.cisnux.dicodingmentoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.data.realtime.RealtimeMentoringDataSource
import dev.cisnux.dicodingmentoring.data.realtime.RealtimeMentoringDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RealtimeMentoringDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindRealtimeMentoringDataSource(
        realtimeDataSourceImpl: RealtimeMentoringDataSourceImpl
    ): RealtimeMentoringDataSource
}