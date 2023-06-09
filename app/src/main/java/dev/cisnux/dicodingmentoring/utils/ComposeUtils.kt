package dev.cisnux.dicodingmentoring.utils

import dev.cisnux.dicodingmentoring.R

data class CheckBoxItem(
    val title: String,
    val checked: Boolean,
    val onCheckedChange: (checked: Boolean, title: String) -> Unit
)

sealed interface UserType {
    object Mentee : UserType
    object Mentor : UserType
}

data class MentoringForm(
    var learningPath: String,
    var experienceLevel: String,
    var skills: String,
    var certificateUrl: String,
    var isLearningPathExpanded: Boolean,
    var isExperienceExpanded: Boolean,
)

val getLearningPathIcon: (learningPath: String) -> Int = { learningPath ->
    val icons = mapOf(
        "Android" to R.drawable.ic_android_24,
        "iOS" to R.drawable.ic_ios,
        "Front-End" to R.drawable.ic_front_end,
        "Back-End" to R.drawable.ic_back_end,
        "Cloud Computing" to R.drawable.ic_cloud_computing,
        "Machine Learning" to R.drawable.ic_machine_learning,
    )
    icons[learningPath] ?: R.drawable.ic_ui_ux
}