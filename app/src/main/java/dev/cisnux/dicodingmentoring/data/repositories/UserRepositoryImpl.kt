package dev.cisnux.dicodingmentoring.data.repositories

import arrow.core.Either
import dev.cisnux.dicodingmentoring.data.remote.MenteeRemoteDataSource
import dev.cisnux.dicodingmentoring.data.remote.MentorRemoteDataSource
import dev.cisnux.dicodingmentoring.data.services.ExpertisesItem
import dev.cisnux.dicodingmentoring.data.services.FileService
import dev.cisnux.dicodingmentoring.data.services.MentorBodyRequest
import dev.cisnux.dicodingmentoring.data.services.asExpertises
import dev.cisnux.dicodingmentoring.data.services.asGetMentors
import dev.cisnux.dicodingmentoring.domain.models.AddMentor
import dev.cisnux.dicodingmentoring.domain.models.AddUserProfile
import dev.cisnux.dicodingmentoring.domain.models.GetMentor
import dev.cisnux.dicodingmentoring.domain.models.GetUserProfile
import dev.cisnux.dicodingmentoring.domain.models.asExpertiseItems
import dev.cisnux.dicodingmentoring.domain.repositories.UserRepository
import dev.cisnux.dicodingmentoring.utils.Failure
import dev.cisnux.dicodingmentoring.utils.HTTP_FAILURES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val menteeRemoteDataSource: MenteeRemoteDataSource,
    private val fileService: FileService,
    private val mentorRemoteDataSource: MentorRemoteDataSource
) : UserRepository {
    override suspend fun getMenteeProfileById(id: String): Either<Exception, GetUserProfile> =
        withContext(Dispatchers.IO) {
            try {
                val userProfileResponse = menteeRemoteDataSource.getMenteeProfileById(id).data
                val expertises: List<ExpertisesItem> =
                    if (userProfileResponse.isMentorValid)
                        mentorRemoteDataSource.getMentorProfileById(id).data.expertises
                    else emptyList()
                val getUserProfile = GetUserProfile(
                    id = userProfileResponse.id,
                    photoProfile = userProfileResponse.photoProfile,
                    fullName = userProfileResponse.fullName,
                    username = userProfileResponse.username,
                    email = userProfileResponse.email,
                    job = userProfileResponse.job,
                    about = userProfileResponse.about,
                    isMentorValid = userProfileResponse.isMentorValid,
                    expertises = expertises.asExpertises()
                )
                Either.Right(getUserProfile)
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

    override suspend fun getMentorProfileById(id: String): Either<Exception, GetUserProfile> =
        withContext(Dispatchers.IO) {
            try {
                val userProfileResponse = menteeRemoteDataSource.getMenteeProfileById(id).data
                val expertises: List<ExpertisesItem> =
                    mentorRemoteDataSource.getMentorProfileById(id).data.expertises
                val getUserProfile = GetUserProfile(
                    id = userProfileResponse.id,
                    photoProfile = userProfileResponse.photoProfile,
                    fullName = userProfileResponse.fullName,
                    username = userProfileResponse.username,
                    email = userProfileResponse.email,
                    job = userProfileResponse.job,
                    about = userProfileResponse.about,
                    isMentorValid = userProfileResponse.isMentorValid,
                    expertises = expertises.asExpertises()
                )
                Either.Right(getUserProfile)
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

    override suspend fun addMenteeProfile(userProfile: AddUserProfile): Either<Exception, Nothing?> =
        withContext(Dispatchers.IO) {
            try {
                val photoFile = userProfile.photoProfileUri?.let {
                    val file = fileService.uriToFile(it)
                    fileService.reduceImage(file)
                }
                val id = userProfile.id
                val fullName = userProfile.fullName.toRequestBody("text/plain".toMediaType())
                val username = userProfile.username.toRequestBody("text/plain".toMediaType())
                val email = userProfile.email.toRequestBody("text/plain".toMediaType())
                val job = userProfile.job.toRequestBody("text/plain".toMediaType())
                val about = userProfile.about.toRequestBody("text/plain".toMediaType())
                val requestImageFile = photoFile?.asRequestBody("image/jpg".toMediaType())
                val imageMultipart = requestImageFile?.let {
                    MultipartBody.Part.createFormData(
                        "photoProfile",
                        photoFile.name,
                        it
                    )
                }
                menteeRemoteDataSource.postMenteeProfile(
                    id,
                    imageMultipart,
                    fullName,
                    username,
                    email,
                    job,
                    about,
                )
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

    override suspend fun addMentorProfile(
        addMentor: AddMentor
    ): Either<Exception, Nothing?> =
        withContext(Dispatchers.IO) {
            try {
                val expertiseItems = addMentor.expertises.asExpertiseItems()
                val mentorBodyRequest = MentorBodyRequest(
                    expertises = expertiseItems
                )
                mentorRemoteDataSource.addMentorProfile(addMentor.id, mentorBodyRequest)
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

    override suspend fun getMentors(
        id: String,
        keyword: String?
    ): Either<Exception, List<GetMentor>> =
        withContext(Dispatchers.IO) {
            try {
                val mentors = mentorRemoteDataSource
                    .getMentors(id, keyword)
                    .mentorListData
                    .mentors
                    .asGetMentors()
                Either.Right(mentors)
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

    override suspend fun getMatchmakingMentors(
        userId: String,
        fullName: String,
        needs: String,
    ): Either<Exception, List<GetMentor>> =
        withContext(Dispatchers.IO)
        {
            try {
                val mentors = mentorRemoteDataSource
                    .getMatchmakingMentors(userId, fullName, needs)
                    .mentors
                    .asGetMentors()
                Either.Right(mentors)
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