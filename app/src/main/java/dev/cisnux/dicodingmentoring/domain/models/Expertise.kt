package dev.cisnux.dicodingmentoring.domain.models

import dev.cisnux.dicodingmentoring.data.services.ExpertisesItem

data class Expertise(
    val learningPath: String,
    val experienceLevel: String,
    val skills: List<String>,
    val certificates: List<String>,
)

fun List<Expertise>.asExpertiseItems() = map {
    ExpertisesItem(
        learningPath = it.learningPath,
        experienceLevel = it.experienceLevel,
        skills = it.skills,
        certificates = it.certificates
    )
}