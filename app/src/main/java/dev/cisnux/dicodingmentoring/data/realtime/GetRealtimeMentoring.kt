package dev.cisnux.dicodingmentoring.data.realtime

import dev.cisnux.dicodingmentoring.domain.models.GetMentoringSession
import kotlinx.serialization.Serializable

@Serializable
data class GetRealtimeMentoring(
    val id: String,
    val title: String,
    val description: String,
    val isOnlyChat: Boolean,
    val eventTime: Long,
)

fun List<GetRealtimeMentoring>.asGetMentoringSessions() =
    map { (id, title, description, isOnlyChat, eventTime) ->
        GetMentoringSession(
            id, title, description, isOnlyChat, eventTime
        )
    }
