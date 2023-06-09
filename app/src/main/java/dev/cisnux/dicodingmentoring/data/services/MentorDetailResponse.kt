package dev.cisnux.dicodingmentoring.data.services

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.cisnux.dicodingmentoring.domain.models.Expertise

data class MentorDetailResponse(
	@Json(name="data")
	val data: MentorProfileData
)

data class MentorProfileData(
	@Json(name="expertises")
	val expertises: List<ExpertisesItem>,
)

@JsonClass(generateAdapter = true)
data class ExpertisesItem(

	@Json(name="skills")
	val skills: List<String>,

	@Json(name="experienceLevel")
	val experienceLevel: String,

	@Json(name="certificates")
	val certificates: List<String>,

	@Json(name="learningPath")
	val learningPath: String
)

fun List<ExpertisesItem>.asExpertises() = map {
	Expertise(
		skills = it.skills,
		learningPath =it.learningPath,
		experienceLevel = it.experienceLevel,
		certificates = it.certificates,
	)
}
