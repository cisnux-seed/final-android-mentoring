package dev.cisnux.dicodingmentoring.data.repositories

import arrow.core.Either
import dev.cisnux.dicodingmentoring.data.remote.TokenMessagingRemoteDataSource
import dev.cisnux.dicodingmentoring.domain.models.UpdateMessagingToken
import dev.cisnux.dicodingmentoring.domain.models.asCloudMessagingRequestBody
import dev.cisnux.dicodingmentoring.utils.Failure
import dev.cisnux.dicodingmentoring.utils.HTTP_FAILURES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TokenMessagingRepositoryImpl @Inject constructor(
    private val remoteDataSource: TokenMessagingRemoteDataSource
) : TokenMessagingRepository {
    override suspend fun updateDeviceToken(updateMessagingToken: UpdateMessagingToken): Either<Exception, Nothing?> =
        withContext(
            Dispatchers.IO
        ) {
            try {
                remoteDataSource.updateDeviceToken(updateMessagingToken.asCloudMessagingRequestBody())
                Either.Right(null)
            } catch (e: IOException) {
                Either.Left(Failure.ConnectionFailure("No internet connection"))
            } catch (e: HttpException) {
                val statusCode = e.response()?.code()
                val failure = HTTP_FAILURES[statusCode]
                val errorBody = e.response()?.errorBody()?.string()
                errorBody?.let {
                    failure?.message = JSONObject(it).getString("message")
                }
                Either.Left(failure ?: e)
            } catch (e: Exception) {
                Either.Left(e)
            }
        }


}