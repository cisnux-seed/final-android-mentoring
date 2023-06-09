package dev.cisnux.dicodingmentoring.domain.repositories

import dev.cisnux.dicodingmentoring.data.realtime.AddChat
import dev.cisnux.dicodingmentoring.data.realtime.RealtimeChats
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getRealtimeChats(userId: String, roomChatId: String): Flow<RealtimeChats>
    suspend fun sentMessage(addChat: AddChat)
    suspend fun onCloseSocket()
}