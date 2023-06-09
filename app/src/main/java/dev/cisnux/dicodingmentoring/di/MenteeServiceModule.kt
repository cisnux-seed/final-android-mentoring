package dev.cisnux.dicodingmentoring.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.data.services.MenteeService
import dev.cisnux.dicodingmentoring.utils.HTTP_BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MenteeServiceModule {

    @Singleton
    @Provides
    fun provideMenteeService(
        client: OkHttpClient,
        moshiConverter: MoshiConverterFactory,
    ): MenteeService {
        return Retrofit.Builder()
            .baseUrl(HTTP_BASE_URL)
            .addConverterFactory(moshiConverter)
            .client(client)
            .build()
            .create(MenteeService::class.java)
    }
}