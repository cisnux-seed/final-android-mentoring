package dev.cisnux.dicodingmentoring.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dicodingmentoring.AuthenticatedUser
import dev.cisnux.dicodingmentoring.data.local.AuthenticatedUserSerializer
import javax.inject.Singleton

private const val appPackageName = "dev.cisnux.dicoding.mentoring.preferences"
private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    appPackageName
)
private const val DATA_STORE_FILE_NAME = "authenticated_user.pb"
private val Context.authenticatedUser: DataStore<AuthenticatedUser> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = AuthenticatedUserSerializer
)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun providePreferenceDataStore(
        @ApplicationContext applicationContext: Context
    ): DataStore<Preferences> = applicationContext.preferencesDataStore

    @Singleton
    @Provides
    fun provideAuthenticatedUserDataStore(
        @ApplicationContext applicationContext: Context
    ): DataStore<AuthenticatedUser> = applicationContext.authenticatedUser
}