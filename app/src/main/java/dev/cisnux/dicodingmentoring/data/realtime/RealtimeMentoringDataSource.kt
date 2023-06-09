package dev.cisnux.dicodingmentoring.data.realtime

import kotlinx.coroutines.flow.Flow

interface RealtimeMentoringDataSource {
    fun getRealtimeMentoringSessions(userId: String): Flow<List<GetRealtimeMentoring>>

    suspend fun createRealtimeMentoring(createRealtimeMentoring: CreateRealtimeMentoring)
    fun getRealtimeDetailMentoring(
        userId: String,
        mentoringId: String
    ): Flow<GetRealtimeDetailMentoring>
    suspend fun acceptMentoring(acceptMentoring: AcceptMentoring)
    suspend fun closeDetailMentoringSocket()
    suspend fun closeMentoringSocket()
}