package dev.cisnux.dicodingmentoring.data.realtime

import android.util.Log
import dev.cisnux.dicodingmentoring.utils.WS_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatRealtimeDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : ChatRealtimeDataSource {
    private var roomSocket: WebSocketSession? = null

    override fun getRealtimeChats(userId: String, roomChatId: String): Flow<RealtimeChats> = flow {
        roomSocket = client.webSocketSession {
            url("$WS_BASE_URL/chat?roomChatId=$roomChatId&userId=$userId")
        }
        val roomChats = roomSocket!!
            .incoming
            .consumeAsFlow()
            .filterIsInstance<Frame.Text>()
            .mapNotNull {
                Log.d("realtime", it.toString())
                Json.Default.decodeFromString<RealtimeChats>(it.readText())
            }
        emitAll(roomChats)
    }

    override suspend fun sentMessage(addChat: AddChat): Unit = withContext(Dispatchers.IO) {
        try {
            Log.d("roomSocket", (roomSocket == null).toString())
            roomSocket?.send(
                Frame.Text(Json.encodeToString(addChat))
            )
        } catch (e: Exception) {
            Log.d("onError", e.stackTraceToString())
        }
    }

    override suspend fun onCloseSocket() {
        roomSocket?.close()
        roomSocket = null
    }
}