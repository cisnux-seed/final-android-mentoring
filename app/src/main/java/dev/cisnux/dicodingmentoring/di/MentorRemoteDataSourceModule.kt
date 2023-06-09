package dev.cisnux.dicodingmentoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.data.remote.MentorRemoteDataSource
import dev.cisnux.dicodingmentoring.data.remote.MentorRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MentorRemoteDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindMentorRemoteDataSource(
        mentorRemoteDataSourceImpl: MentorRemoteDataSourceImpl
    ): MentorRemoteDataSource
}