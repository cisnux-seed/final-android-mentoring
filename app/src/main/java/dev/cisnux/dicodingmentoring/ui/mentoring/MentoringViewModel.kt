package dev.cisnux.dicodingmentoring.ui.mentoring

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.domain.models.GetMentoringSession
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.domain.repositories.MentoringRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MentoringViewModel @Inject constructor(
    private val mentoringRepository: MentoringRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _showConnectionError = mutableStateOf(false)
    val showConnectionError: State<Boolean> get() = _showConnectionError
    private val _getMentoringSessions = MutableStateFlow<List<GetMentoringSession>>(emptyList())
    val getMentoringSessions: StateFlow<List<GetMentoringSession>>
        get() = _getMentoringSessions

    fun subscribeMentoringSessions() = viewModelScope.launch {
        authRepository.currentUser().catch {
            _showConnectionError.value = true
        }.collectLatest {
            it.uid?.let { id ->
                if (id.isNotBlank()) {
                    mentoringRepository.getMentoringSessions(id)
                        .collectLatest { mentoringSessions ->
                            _getMentoringSessions.value = mentoringSessions
                        }
                }
            }
        }
    }
    override fun onCleared() {
        super.onCleared()

        viewModelScope.launch {
            mentoringRepository.closeMentoringSockets()
        }
    }
}