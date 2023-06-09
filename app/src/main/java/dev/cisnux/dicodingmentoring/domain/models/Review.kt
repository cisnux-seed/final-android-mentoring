package dev.cisnux.dicodingmentoring.domain.models

data class Review(
    val fullName: String,
    val photoProfile: String? = null,
    val comment: String,
    val rating: Float,
)
