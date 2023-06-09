package dev.cisnux.dicodingmentoring.data.remote

import dev.cisnux.dicodingmentoring.data.services.MenteeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MenteeRemoteDataSource {
    suspend fun getMenteeProfileById(id: String): MenteeResponse
    suspend fun postMenteeProfile(
        id: String,
        photoProfile: MultipartBody.Part?,
        fullName: RequestBody,
        username: RequestBody,
        email: RequestBody,
        job: RequestBody,
        about: RequestBody,
    )
}