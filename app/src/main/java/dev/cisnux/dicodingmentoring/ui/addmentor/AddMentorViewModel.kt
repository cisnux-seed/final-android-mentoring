package dev.cisnux.dicodingmentoring.ui.addmentor

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.cisnux.dicodingmentoring.DicodingMentoringApplication
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.domain.models.AddMentor
import dev.cisnux.dicodingmentoring.domain.models.Expertise
import dev.cisnux.dicodingmentoring.domain.repositories.UserRepository
import dev.cisnux.dicodingmentoring.utils.MentoringForm
import dev.cisnux.dicodingmentoring.utils.UiState
import dev.cisnux.dicodingmentoring.utils.asList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMentorViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(appContext as Application) {
    private val context = getApplication<DicodingMentoringApplication>()
    private val id = checkNotNull(savedStateHandle["id"]) as String
    private val _addMentorState = mutableStateOf<UiState<Nothing?>>(UiState.Initialize)
    val addMentorState: State<UiState<Nothing?>> get() = _addMentorState
    private val _learningPathOptions =
        mutableStateListOf(
            context.getString(R.string.ios),
            context.getString(R.string.frontend),
            context.getString(R.string.backend),
            context.getString(R.string.cloud_computing),
            context.getString(R.string.machine_learning),
            context.getString(R.string.ui_ux),
        )
    val maxFormSize = _learningPathOptions.size + 1
    val learningPathOptions get() = _learningPathOptions
    private val _mentoringForms = mutableStateListOf(
        MentoringForm(
            learningPath = context.getString(R.string.android),
            experienceLevel = context.getString(R.string.beginner),
            skills = "",
            certificateUrl = "",
            isLearningPathExpanded = false,
            isExperienceExpanded = false,
        )
    )
    val mentoringForms get() = _mentoringForms

    fun onAddForm(selectedLearningPath: String) {
        // remove to prev options
        _learningPathOptions.remove(selectedLearningPath)
        val learningPath = _learningPathOptions.first()
        // remove to next options
        _learningPathOptions.remove(learningPath)
        mentoringForms.add(
            MentoringForm(
                learningPath = learningPath,
                experienceLevel = context.getString(R.string.beginner),
                skills = "",
                certificateUrl = "",
                isLearningPathExpanded = false,
                isExperienceExpanded = false,
            )
        )
    }

    fun onRemoveForm(selectedLearningPath: String) {
        _learningPathOptions.add(selectedLearningPath)
        mentoringForms.removeLast()
    }


    fun onExperienceLevelOption(experienceLevel: String, index: Int) {
        mentoringForms[index] =
            mentoringForms[index].copy(experienceLevel = experienceLevel)
    }

    fun onExpandedLearningChanged(isExpanded: Boolean, index: Int) {
        mentoringForms[index] =
            mentoringForms[index].copy(isLearningPathExpanded = if (_learningPathOptions.isEmpty()) false else isExpanded)
    }

    fun onExpandedExperienceChanged(isExpanded: Boolean, index: Int) {
        mentoringForms[index] =
            mentoringForms[index].copy(isExperienceExpanded = isExpanded)
    }

    fun onLearningPathOption(learningPath: String, index: Int) {
        // remove old value
        _learningPathOptions.remove(learningPath)
        // add new value
        _learningPathOptions.add(mentoringForms[index].learningPath)
        mentoringForms[index] =
            mentoringForms[index].copy(learningPath = learningPath)
    }

    fun onSkillChanged(skill: String, index: Int) {
        mentoringForms[index] = mentoringForms[index].copy(skills = skill)
    }

    fun onCertificateUrlChanged(certificateUrl: String, index: Int) {
        mentoringForms[index] =
            mentoringForms[index].copy(certificateUrl = certificateUrl)
    }

    fun onCloseLearning(index: Int) {
        mentoringForms[index] =
            mentoringForms[index].copy(isLearningPathExpanded = false)
    }

    fun onCloseExperience(index: Int) {
        mentoringForms[index] =
            mentoringForms[index].copy(isExperienceExpanded = false)
    }

    fun onJoin() = viewModelScope.launch {
        val addMentor = AddMentor(
            id = id,
            expertises = _mentoringForms.map {
                Expertise(
                    learningPath = it.learningPath,
                    experienceLevel = it.experienceLevel,
                    skills = it.skills.asList(),
                    certificates = it.certificateUrl.asList()
                )
            }
        )
        val result = userRepository.addMentorProfile(addMentor = addMentor)
        result.fold({
            _addMentorState.value = UiState.Error(it)
        }, {
            _addMentorState.value = UiState.Success()
        })
    }
}