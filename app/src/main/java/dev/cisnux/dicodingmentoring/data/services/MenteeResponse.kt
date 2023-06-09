package dev.cisnux.dicodingmentoring.data.services

import com.squareup.moshi.Json

data class MenteeResponse(

    @Json(name = "data")
    val data: MenteeProfileData
)

data class MenteeProfileData(
    @Json(name = "id")
    val id: String,

    @Json(name = "fullName")
    val fullName: String,

    @Json(name = "isMentorValid")
    val isMentorValid: Boolean,

    @Json(name = "job")
    val job: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "username")
    val username: String,

    @Json(name = "photoProfile")
    val photoProfile: String? = null,

    @Json(name = "about")
    val about: String,
)
