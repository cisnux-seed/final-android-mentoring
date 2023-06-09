package dev.cisnux.dicodingmentoring.data.services

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CloudMessagingBodyRequest(
    @Json(name = "userId")
    val userId: String,

    @Json(name = "deviceToken")
    val deviceToken: String,
)
