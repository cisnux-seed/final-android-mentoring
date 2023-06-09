package dev.cisnux.dicodingmentoring.domain.models

import android.net.Uri

data class AddUserProfile(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String,
    val job: String,
    val photoProfileUri: Uri? = null,
    val about: String,
)
