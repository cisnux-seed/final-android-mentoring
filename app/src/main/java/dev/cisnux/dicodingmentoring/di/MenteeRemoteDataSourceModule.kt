package dev.cisnux.dicodingmentoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.data.remote.MenteeRemoteDataSource
import dev.cisnux.dicodingmentoring.data.remote.MenteeRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MenteeRemoteDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindMenteeRemoteDataSource(
        menteeRemoteDataSourceImpl: MenteeRemoteDataSourceImpl
    ): MenteeRemoteDataSource
}