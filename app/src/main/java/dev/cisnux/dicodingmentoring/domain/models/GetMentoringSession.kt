package dev.cisnux.dicodingmentoring.domain.models

data class GetMentoringSession(
    val id: String,
    val title: String,
    val description: String,
    val isOnlyChat: Boolean,
    val eventTime: Long
)
