package dev.cisnux.dicodingmentoring.domain.models

import dev.cisnux.dicodingmentoring.data.realtime.CreateRealtimeMentoring

data class AddMentoringSession(
    val mentorId: String,
    val menteeId: String,
    val title: String,
    val description: String,
    val eventTime: Long,
    val isOnlyChat: Boolean,
)

fun AddMentoringSession.asCreateRealtimeMentoring() =
    CreateRealtimeMentoring(
        mentorId,
        menteeId,
        title,
        description,
        isOnlyChat,
        eventTime,
    )
