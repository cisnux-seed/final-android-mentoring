package dev.cisnux.dicodingmentoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.data.repositories.MentoringRepositoryImpl
import dev.cisnux.dicodingmentoring.domain.repositories.MentoringRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MentoringRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMentoringRepository(
        mentoringRepositoryImpl: MentoringRepositoryImpl
    ): MentoringRepository
}