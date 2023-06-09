package dev.cisnux.dicodingmentoring.data.repositories

import arrow.core.Either
import dev.cisnux.dicodingmentoring.domain.models.UpdateMessagingToken

interface TokenMessagingRepository {
    suspend fun updateDeviceToken(updateMessagingToken: UpdateMessagingToken): Either<Exception, Nothing?>
}