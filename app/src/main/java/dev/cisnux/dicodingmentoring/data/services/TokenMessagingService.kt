package dev.cisnux.dicodingmentoring.data.services

import retrofit2.http.Body
import retrofit2.http.PUT

interface TokenMessagingService {

    @PUT("/ws/cloudMessaging")
    suspend fun updateDeviceToken(
        @Body cloudMessagingBodyRequest: CloudMessagingBodyRequest,
    )
}