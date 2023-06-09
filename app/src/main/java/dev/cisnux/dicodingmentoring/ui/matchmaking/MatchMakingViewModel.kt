package dev.cisnux.dicodingmentoring.ui.matchmaking

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.domain.models.GetMentor
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.domain.repositories.UserRepository
import dev.cisnux.dicodingmentoring.utils.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchMakingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _mentorsState = mutableStateOf<UiState<List<GetMentor>>>(UiState.Initialize)
    val mentorsState: State<UiState<List<GetMentor>>> get() = _mentorsState
    private val needs = checkNotNull(savedStateHandle["needs"]) as String

    init {
        viewModelScope.launch {
            authRepository.currentUser().collectLatest {
                if (it.uid.isNotBlank()) {
                    _mentorsState.value = UiState.Loading
                    val result = userRepository.getMatchmakingMentors(
                        userId = it.uid,
                        fullName = it.email,
                        needs = needs
                    )
                    result.fold(
                        { exception ->
                            _mentorsState.value = UiState.Error(exception)
                        },
                        { mentors ->
                            _mentorsState.value = UiState.Success(mentors)
                        }
                    )
                }
            }
        }
    }

}