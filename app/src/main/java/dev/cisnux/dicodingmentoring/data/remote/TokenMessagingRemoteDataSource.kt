package dev.cisnux.dicodingmentoring.data.remote

import dev.cisnux.dicodingmentoring.data.services.CloudMessagingBodyRequest

interface TokenMessagingRemoteDataSource {
    suspend fun updateDeviceToken(cloudMessagingBodyRequest: CloudMessagingBodyRequest)
}