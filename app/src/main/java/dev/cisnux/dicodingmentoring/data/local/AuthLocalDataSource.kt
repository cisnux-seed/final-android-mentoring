package dev.cisnux.dicodingmentoring.data.local

import dev.cisnux.dicodingmentoring.AuthenticatedUser
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    fun getAuthSession(id: String): Flow<Boolean>
    suspend fun saveAuthSession(id: String, session: Boolean)
    suspend fun deleteSession(id: String)
    val authenticatedUser: Flow<AuthenticatedUser>
    suspend fun deleteAuthenticatedUser()
    suspend fun saveAuthenticatedUser(uid: String, email: String)
}