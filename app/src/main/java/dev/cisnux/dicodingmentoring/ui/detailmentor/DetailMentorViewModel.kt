package dev.cisnux.dicodingmentoring.ui.detailmentor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.domain.models.GetUserProfile
import dev.cisnux.dicodingmentoring.domain.repositories.UserRepository
import dev.cisnux.dicodingmentoring.utils.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMentorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {
    private val id = checkNotNull(savedStateHandle["id"]) as String
    private val _mentorDetailState = mutableStateOf<UiState<GetUserProfile>>(UiState.Initialize)
    val mentorDetailState: State<UiState<GetUserProfile>> get() = _mentorDetailState


    fun getMentorProfile() = viewModelScope.launch {
        _mentorDetailState.value = UiState.Loading
        val result = userRepository.getMenteeProfileById(id)
        result.fold({ exception ->
            _mentorDetailState.value = UiState.Error(exception)
        }, { getUserProfile ->
            _mentorDetailState.value = UiState.Success(getUserProfile)
        })
    }
}