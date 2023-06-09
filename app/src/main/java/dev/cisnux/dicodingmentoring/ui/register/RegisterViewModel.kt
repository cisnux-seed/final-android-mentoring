package dev.cisnux.dicodingmentoring.ui.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.domain.models.AuthUser
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _registerState = MutableStateFlow<UiState<String?>>(UiState.Initialize)
    val registerState: StateFlow<UiState<String?>> get() = _registerState

    private val _email = mutableStateOf("")
    val email: State<String> get() = _email

    private val _password = mutableStateOf("")
    val password: State<String> get() = _password

    fun register() = viewModelScope.launch {
        _registerState.value = UiState.Loading
        val authUser = AuthUser(email = _email.value, password = _password.value)
        val result = repository.register(authUser)
        result.fold(
            {
                _registerState.value = UiState.Error(it)
            }, {
                _registerState.value = UiState.Success(it)
            }
        )
    }

    fun onEmailQueryChanged(email: String) {
        _email.value = email
    }

    fun onPasswordQueryChanged(password: String) {
        _password.value = password
    }
}