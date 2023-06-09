package dev.cisnux.dicodingmentoring.data.repositories

import dev.cisnux.dicodingmentoring.data.realtime.AcceptMentoring
import dev.cisnux.dicodingmentoring.data.realtime.GetRealtimeDetailMentoring
import dev.cisnux.dicodingmentoring.data.realtime.RealtimeMentoringDataSource
import dev.cisnux.dicodingmentoring.data.realtime.asGetMentoringSessions
import dev.cisnux.dicodingmentoring.domain.models.AddMentoringSession
import dev.cisnux.dicodingmentoring.domain.models.GetMentoringSession
import dev.cisnux.dicodingmentoring.domain.models.asCreateRealtimeMentoring
import dev.cisnux.dicodingmentoring.domain.repositories.MentoringRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MentoringRepositoryImpl @Inject constructor(
    private val dataSource: RealtimeMentoringDataSource
) : MentoringRepository {
    override fun getMentoringSessions(userId: String): Flow<List<GetMentoringSession>> =
        dataSource.getRealtimeMentoringSessions(userId).map {
            it.asGetMentoringSessions()
        }

    override suspend fun createMentoring(addMentoringSession: AddMentoringSession) =
        dataSource.createRealtimeMentoring(addMentoringSession.asCreateRealtimeMentoring())

    override suspend fun closeMentoringSockets() =
        dataSource.closeMentoringSocket()

    override fun getRealtimeDetailMentoring(
        userId: String,
        mentoringId: String
    ): Flow<GetRealtimeDetailMentoring> = dataSource.getRealtimeDetailMentoring(userId, mentoringId)

    override suspend fun acceptMentoring(acceptMentoring: AcceptMentoring) =
        dataSource.acceptMentoring(acceptMentoring)

    override suspend fun closeDetailMentoringSocket() = dataSource.closeDetailMentoringSocket()
}