package dev.cisnux.dicodingmentoring.data.remote

import dev.cisnux.dicodingmentoring.data.services.CloudMessagingBodyRequest
import dev.cisnux.dicodingmentoring.data.services.TokenMessagingService
import javax.inject.Inject

class TokenMessagingRemoteDataSourceImpl @Inject constructor(
    private val tokenMessagingService: TokenMessagingService,
) : TokenMessagingRemoteDataSource {
    override suspend fun updateDeviceToken(cloudMessagingBodyRequest: CloudMessagingBodyRequest) =
        tokenMessagingService.updateDeviceToken(cloudMessagingBodyRequest)
}