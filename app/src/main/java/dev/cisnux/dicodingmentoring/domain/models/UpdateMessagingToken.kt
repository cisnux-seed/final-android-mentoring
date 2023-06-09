package dev.cisnux.dicodingmentoring.domain.models

import dev.cisnux.dicodingmentoring.data.services.CloudMessagingBodyRequest

data class UpdateMessagingToken(
    val userId: String,
    val deviceToken: String,
)

fun UpdateMessagingToken.asCloudMessagingRequestBody() =
    CloudMessagingBodyRequest(
        userId = userId,
        deviceToken = deviceToken
    )
