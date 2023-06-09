package dev.cisnux.dicodingmentoring.data.services

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MentorBodyRequest(
    @Json(name = "expertises")
    val expertises: List<ExpertisesItem>,
)

