package dev.cisnux.dicodingmentoring.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.cisnux.dicodingmentoring.AuthenticatedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthLocalDataSourceImpl
@Inject constructor(
    private val sessionDataStore: DataStore<Preferences>,
    private val authUserDataStore: DataStore<AuthenticatedUser>
) : AuthLocalDataSource {

    override fun getAuthSession(id: String): Flow<Boolean> {
        return sessionDataStore.data.map { preference ->
            val key = booleanPreferencesKey(id)
            val data = preference[key] ?: false
            data
        }
    }

    override suspend fun saveAuthSession(id: String, session: Boolean): Unit =
        withContext(Dispatchers.IO) {
            val key = booleanPreferencesKey(id)
            sessionDataStore.edit { preference ->
                preference[key] = session
            }
        }

    override suspend fun deleteSession(id: String): Unit = withContext(Dispatchers.IO) {
        val key = booleanPreferencesKey(id)
        sessionDataStore.edit { preference ->
            preference.remove(key)
        }
    }

    override val authenticatedUser: Flow<AuthenticatedUser>
        get() = authUserDataStore.data

    override suspend fun deleteAuthenticatedUser(): Unit =
        withContext(Dispatchers.IO) {
            authUserDataStore.updateData {currentUser->
                currentUser
                    .toBuilder()
                    .clear()
                    .build()
            }
        }

    override suspend fun saveAuthenticatedUser(uid: String, email: String): Unit =
        withContext(Dispatchers.IO) {
            authUserDataStore.updateData {currentUser->
                currentUser
                    .toBuilder()
                    .setUid(uid)
                    .setEmail(email)
                    .build()
            }
        }
}