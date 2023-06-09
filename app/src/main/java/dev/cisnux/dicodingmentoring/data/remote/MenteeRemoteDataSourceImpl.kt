package dev.cisnux.dicodingmentoring.data.remote

import dev.cisnux.dicodingmentoring.data.services.MenteeResponse
import dev.cisnux.dicodingmentoring.data.services.MenteeService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MenteeRemoteDataSourceImpl
@Inject constructor(private val service: MenteeService) :
    MenteeRemoteDataSource {
    override suspend fun getMenteeProfileById(id: String): MenteeResponse =
        service.getMenteeProfileById(id)

    override suspend fun postMenteeProfile(
        id: String,
        photoProfile: MultipartBody.Part?,
        fullName: RequestBody,
        username: RequestBody,
        email: RequestBody,
        job: RequestBody,
        about: RequestBody
    ) = service.postMenteeProfile(
        id,
        photoProfile,
        fullName,
        username,
        email,
        job,
        about
    )
}