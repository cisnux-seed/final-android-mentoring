package dev.cisnux.dicodingmentoring.data.remote

import dev.cisnux.dicodingmentoring.data.services.MentorBodyRequest
import dev.cisnux.dicodingmentoring.data.services.MentorDetailResponse
import dev.cisnux.dicodingmentoring.data.services.MentorListData
import dev.cisnux.dicodingmentoring.data.services.MentorListResponse
import retrofit2.http.Query

interface MentorRemoteDataSource {
    suspend fun getMentorProfileById(id: String): MentorDetailResponse
    suspend fun addMentorProfile(id: String, mentor: MentorBodyRequest)
    suspend fun getMentors(
        id: String,
        keyword: String? = null
    ): MentorListResponse

    suspend fun getMatchmakingMentors(
        userId: String,
        fullName: String,
        needs: String,
    ): MentorListData
}