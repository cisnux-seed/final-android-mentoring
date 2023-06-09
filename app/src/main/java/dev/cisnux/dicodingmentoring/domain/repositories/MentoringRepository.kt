package dev.cisnux.dicodingmentoring.domain.repositories

import dev.cisnux.dicodingmentoring.data.realtime.AcceptMentoring
import dev.cisnux.dicodingmentoring.data.realtime.GetRealtimeDetailMentoring
import dev.cisnux.dicodingmentoring.domain.models.AddMentoringSession
import dev.cisnux.dicodingmentoring.domain.models.GetMentoringSession
import kotlinx.coroutines.flow.Flow

interface MentoringRepository {
    fun getMentoringSessions(userId: String): Flow<List<GetMentoringSession>>
    suspend fun createMentoring(addMentoringSession: AddMentoringSession)
    suspend fun closeMentoringSockets()
    fun getRealtimeDetailMentoring(
        userId: String,
        mentoringId: String
    ): Flow<GetRealtimeDetailMentoring>
    suspend fun acceptMentoring(acceptMentoring: AcceptMentoring)
    suspend fun closeDetailMentoringSocket()
}