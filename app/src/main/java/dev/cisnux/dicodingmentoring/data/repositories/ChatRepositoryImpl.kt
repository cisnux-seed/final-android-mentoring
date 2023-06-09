package dev.cisnux.dicodingmentoring.data.repositories

import dev.cisnux.dicodingmentoring.data.realtime.AddChat
import dev.cisnux.dicodingmentoring.data.realtime.ChatRealtimeDataSource
import dev.cisnux.dicodingmentoring.data.realtime.RealtimeChats
import dev.cisnux.dicodingmentoring.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatRealtimeDataSource: ChatRealtimeDataSource
) : ChatRepository {
    override fun getRealtimeChats(userId: String, roomChatId: String): Flow<RealtimeChats> =
        chatRealtimeDataSource.getRealtimeChats(userId, roomChatId)

    override suspend fun sentMessage(addChat: AddChat) =
        chatRealtimeDataSource.sentMessage(addChat)

    override suspend fun onCloseSocket() = chatRealtimeDataSource.onCloseSocket()
}