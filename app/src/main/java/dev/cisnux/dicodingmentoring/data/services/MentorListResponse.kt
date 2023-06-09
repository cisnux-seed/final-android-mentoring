package dev.cisnux.dicodingmentoring.data.services

import com.squareup.moshi.Json
import dev.cisnux.dicodingmentoring.domain.models.GetMentor

data class MentorListResponse(

	@Json(name="data")
	val mentorListData: MentorListData
)

data class MentorListData(

	@Json(name="mentors")
	val mentors: List<MentorsItem>
)

data class MentorsItem(

	@Json(name="photoProfile")
	val photoProfile: String?,

	@Json(name="averageRating")
	val averageRating: Double,

	@Json(name="fullName")
	val fullName: String,

	@Json(name="id")
	val id: String,

	@Json(name="job")
	val job: String
)

fun List<MentorsItem>.asGetMentors() = map{
	GetMentor(
		id = it.id,
		fullName = it.fullName,
		photoProfile = it.photoProfile,
		job = it.job,
		averageRating = it.averageRating
	)
}
