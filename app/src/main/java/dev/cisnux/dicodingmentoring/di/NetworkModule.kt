package dev.cisnux.dicodingmentoring.di
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
    ): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor()
                .setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}