package dev.cisnux.dicodingmentoring.domain.repositories

import arrow.core.Either
import dev.cisnux.dicodingmentoring.domain.models.AddMentor
import dev.cisnux.dicodingmentoring.domain.models.AddUserProfile
import dev.cisnux.dicodingmentoring.domain.models.GetMentor
import dev.cisnux.dicodingmentoring.domain.models.GetUserProfile

interface UserRepository {
    suspend fun getMenteeProfileById(id: String): Either<Exception, GetUserProfile>
    suspend fun getMentorProfileById(id: String): Either<Exception, GetUserProfile>
    suspend fun addMenteeProfile(
        userProfile: AddUserProfile
    ): Either<Exception, Nothing?>

    suspend fun addMentorProfile(
        addMentor: AddMentor
    ): Either<Exception, Nothing?>

    suspend fun getMentors(
        id: String,
        keyword: String? = null,
    ): Either<Exception, List<GetMentor>>

    suspend fun getMatchmakingMentors(
        userId: String,
        fullName: String,
        needs: String,
    ): Either<Exception, List<GetMentor>>
}