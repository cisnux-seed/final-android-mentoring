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


class RealtimeMentoringDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : RealtimeMentoringDataSource {
    private var mentoringSession: WebSocketSession? = null
    private var detailMentoring: WebSocketSession? = null

    override fun getRealtimeMentoringSessions(userId: String): Flow<List<GetRealtimeMentoring>> =
        flow {
            mentoringSession = client.webSocketSession {
                url("$WS_BASE_URL/mentoring?userId=$userId")
            }
            val mentoringSessions = mentoringSession!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull {
                    Json.Default.decodeFromString<List<GetRealtimeMentoring>>(it.readText())
                }
            emitAll(mentoringSessions)
        }

    override fun getRealtimeDetailMentoring(
        userId: String,
        mentoringId: String
    ): Flow<GetRealtimeDetailMentoring> = flow {
        detailMentoring = client.webSocketSession {
            url("$WS_BASE_URL/detailMentoring?userId=$userId&mentoringId=$mentoringId")
        }
        val detailMentoring = detailMentoring!!
            .incoming
            .consumeAsFlow()
            .filterIsInstance<Frame.Text>()
            .mapNotNull {
                val d = Json.Default.decodeFromString<GetRealtimeDetailMentoring>(it.readText())
                Log.d("detailMentorings", d.toString())
                d
            }
        emitAll(detailMentoring)
    }


    override suspend fun createRealtimeMentoring(createRealtimeMentoring: CreateRealtimeMentoring): Unit =
        withContext(Dispatchers.IO) {
            mentoringSession = client.webSocketSession {
                url("$WS_BASE_URL/mentoring")
            }

            mentoringSession?.send(
                Frame.Text(Json.Default.encodeToString(createRealtimeMentoring))
            )
        }

    override suspend fun acceptMentoring(acceptMentoring: AcceptMentoring): Unit =
        withContext(Dispatchers.IO) {
            detailMentoring?.send(
                Frame.Text(Json.Default.encodeToString(acceptMentoring))
            )
        }

    override suspend fun closeMentoringSocket(): Unit = withContext(Dispatchers.IO) {
        mentoringSession?.close()
        mentoringSession = null
    }

    override suspend fun closeDetailMentoringSocket() {
        detailMentoring?.close()
        detailMentoring = null
    }
}