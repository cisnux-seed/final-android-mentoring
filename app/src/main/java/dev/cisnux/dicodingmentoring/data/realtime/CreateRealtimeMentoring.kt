package dev.cisnux.dicodingmentoring.data.realtime

import kotlinx.serialization.Serializable

@Serializable
data class CreateRealtimeMentoring(
    val mentorId: String,
    val menteeId: String,
    val title: String,
    val description: String,
    val isOnlyChat: Boolean,
    val eventTime: Long,
)
