package dev.cisnux.dicodingmentoring.data.realtime

import kotlinx.serialization.Serializable

@Serializable
data class GetRealtimeDetailMentoring(
    val id: String,
    val mentor: Mentee,
    val mentee: Mentee,
    val title: String,
    val description: String,
    val eventTime: Long,
    val isOnlyChat: Boolean,
    val roomChatId: String?,
    val videoChatLink: String?,
    val isCompleted: Boolean,
    val isAccepted: Boolean,
)

@Serializable
data class Mentee(
    val id: String,
    val fullName: String,
    val photoProfile: String?,
    val email: String
)