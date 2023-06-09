package dev.cisnux.dicodingmentoring.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.data.services.FileService
import dev.cisnux.dicodingmentoring.data.services.FileServiceImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class FileServiceModule {

    @Singleton
    @Binds
    abstract fun bindFileService(
        fileServiceImpl: FileServiceImpl
    ): FileService
}