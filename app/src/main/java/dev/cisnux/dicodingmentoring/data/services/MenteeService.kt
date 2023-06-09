package dev.cisnux.dicodingmentoring.data.services

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MenteeService {
    @Multipart
    @POST("mentees/{id}")
    suspend fun postMenteeProfile(
        @Path("id") id: String,
        @Part photoProfile: MultipartBody.Part?,
        @Part("fullName") fullName: RequestBody,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("job") job: RequestBody,
        @Part("about") about: RequestBody,
    )

    @GET("mentees/{id}")
    suspend fun getMenteeProfileById(
        @Path("id") id: String,
    ): MenteeResponse
}