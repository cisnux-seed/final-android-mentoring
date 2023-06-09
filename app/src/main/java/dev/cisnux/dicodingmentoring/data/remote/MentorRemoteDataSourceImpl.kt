package dev.cisnux.dicodingmentoring.data.remote

import dev.cisnux.dicodingmentoring.data.services.MentorBodyRequest
import dev.cisnux.dicodingmentoring.data.services.MentorDetailResponse
import dev.cisnux.dicodingmentoring.data.services.MentorListData
import dev.cisnux.dicodingmentoring.data.services.MentorListResponse
import dev.cisnux.dicodingmentoring.data.services.MentorService
import javax.inject.Inject

class MentorRemoteDataSourceImpl @Inject constructor(
    private val service: MentorService
) : MentorRemoteDataSource {
    override suspend fun getMentorProfileById(id: String): MentorDetailResponse =
        service.getMentorProfileById(id)

    override suspend fun addMentorProfile(id: String, mentor: MentorBodyRequest): Unit =
        service.postMentorProfile(id, mentor)

    override suspend fun getMentors(
        id: String,
        keyword: String?
    ): MentorListResponse =
        service.getMentors(id, keyword)

    override suspend fun getMatchmakingMentors(
        userId: String,
        fullName: String,
        needs: String,
    ): MentorListData =
        service.getMatchmakingMentors(userId, fullName, needs)
}