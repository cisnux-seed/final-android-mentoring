package dev.cisnux.dicodingmentoring.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.domain.models.GetMentor
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.domain.repositories.UserRepository
import dev.cisnux.dicodingmentoring.utils.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _mentorsState = mutableStateOf<UiState<List<GetMentor>>>(UiState.Initialize)
    private val _query = mutableStateOf("")
    val query: State<String> get() = _query
    val mentorsState: State<UiState<List<GetMentor>>> get() = _mentorsState

    val currentUser get() = authRepository.currentUser()

    fun onQueryChanged(query: String) {
        _query.value = query
    }

    fun getMentors(id: String) = viewModelScope.launch {
        if (id.isNotBlank()) {
            _mentorsState.value = UiState.Loading
            val result = userRepository.getMentors(id)
            result.fold(
                {
                    _mentorsState.value = UiState.Error(it)
                },
                {
                    _mentorsState.value = UiState.Success(it)
                }
            )
        }
    }
}