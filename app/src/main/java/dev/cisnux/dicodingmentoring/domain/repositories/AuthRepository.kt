package dev.cisnux.dicodingmentoring.domain.repositories

import android.content.Intent
import arrow.core.Either
import dev.cisnux.dicodingmentoring.AuthenticatedUser
import dev.cisnux.dicodingmentoring.domain.models.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(user: AuthUser): Either<Exception, String?>
    suspend fun signInWithEmailAndPassword(user: AuthUser): Either<Exception, String?>
    suspend fun resetPassword(email: String): Either<Exception, Nothing?>
    suspend fun signInWithGoogle(googleToken: String?): Either<Exception, String?>
    fun getAuthSession(id: String): Flow<Boolean>
    suspend fun saveAuthSession(id: String, session: Boolean)
    fun getGoogleIntent(): Intent
    suspend fun logout()
    suspend fun logout(id: String)
    fun currentUser(): Flow<AuthenticatedUser>
}