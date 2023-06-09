package dev.cisnux.dicodingmentoring.domain.models

data class GetUserProfile(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String,
    val job: String,
    val isMentorValid: Boolean,
    val about: String,
    val photoProfile: String? = null,
    val expertises: List<Expertise>
)
