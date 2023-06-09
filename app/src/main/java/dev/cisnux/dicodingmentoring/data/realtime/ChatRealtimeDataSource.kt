package dev.cisnux.dicodingmentoring.data.realtime

import kotlinx.coroutines.flow.Flow

interface ChatRealtimeDataSource {
    fun getRealtimeChats(userId: String, roomChatId: String): Flow<RealtimeChats>
    suspend fun sentMessage(addChat: AddChat)
    suspend fun onCloseSocket()
}