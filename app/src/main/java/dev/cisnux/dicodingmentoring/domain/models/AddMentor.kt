package dev.cisnux.dicodingmentoring.domain.models

data class AddMentor(
    val id: String,
    val expertises: List<Expertise>,
)
