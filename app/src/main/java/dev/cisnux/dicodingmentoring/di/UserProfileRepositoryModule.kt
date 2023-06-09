package dev.cisnux.dicodingmentoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.data.repositories.UserRepositoryImpl
import dev.cisnux.dicodingmentoring.domain.repositories.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserProfileRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindUserProfileRepository(
        userProfileRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}