package dev.cisnux.dicodingmentoring.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.domain.repositories.MentoringRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val mentoringRepository: MentoringRepository
) : ViewModel() {
    private val _authSession = MutableStateFlow<Boolean?>(null)
    val authSession get() = _authSession.asStateFlow()

    private val _shouldBottomBarOpen = mutableStateOf(false)
    val shouldBottomBarOpen: State<Boolean> get() = _shouldBottomBarOpen
    private val _isRefreshMyProfileContent = mutableStateOf(false)
    val isRefreshMyProfileContent: State<Boolean> get() = _isRefreshMyProfileContent

    private var currentUserId = authRepository.currentUser().map {
        it.uid
    }

    fun updateBottomState(shouldBottomBarOpen: Boolean) {
        _shouldBottomBarOpen.value = shouldBottomBarOpen
    }

    fun refreshMyProfileContent(refreshMyProfileContent: Boolean) {
        _isRefreshMyProfileContent.value = refreshMyProfileContent
    }

    init {
        viewModelScope.launch {
            _authSession.value = authRepository.getAuthSession(currentUserId.first() ?: "").first()
            if (_authSession.value != true) {
                // invalidate all previous authentications
                authRepository.logout()
            } else {
                // close bottom bar when there is no auth session
                _shouldBottomBarOpen.value = true
            }
        }
    }

    fun logout() = viewModelScope.launch {
        currentUserId.first()?.let { id ->
            mentoringRepository.closeMentoringSockets()
            authRepository.logout(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            mentoringRepository.closeMentoringSockets()
        }
    }
}