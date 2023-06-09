package dev.cisnux.dicodingmentoring.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.domain.models.AuthUser
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.utils.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    val currentUserId get() = repository.currentUser().map {
        it.uid
    }
    val getGoogleIntent get() = repository.getGoogleIntent()
    private val _loginState = MutableStateFlow<UiState<String?>>(UiState.Initialize)
    val loginState get() = _loginState.asStateFlow()

    private val _email = mutableStateOf("")
    val email: State<String> get() = _email

    private val _password = mutableStateOf("")
    val password: State<String> get() = _password

    fun loginWithEmailAndPassword() = viewModelScope.launch {
        _loginState.value = UiState.Loading
        val authUser = AuthUser(email = _email.value, password = _password.value)
        val result = repository.signInWithEmailAndPassword(authUser)
        result.fold(
            {
                _loginState.value = UiState.Error(it)
            },
            {
                _loginState.value = UiState.Success(it)
            }
        )
    }

    fun googleSignIn(token: String?) = viewModelScope.launch {
        _loginState.value = UiState.Initialize
        val result = repository.signInWithGoogle(token)
        result.fold(
            {
                _loginState.value = UiState.Error(it)
            }, {
                _loginState.value = UiState.Success(it)
            }
        )
    }

    fun saveAuthSession(id: String, session: Boolean) = viewModelScope.launch {
        repository.saveAuthSession(id, session)
        delay(500L)
    }

    fun onEmailQueryChanged(email: String) {
        _email.value = email
    }

    fun onPasswordQueryChanged(password: String) {
        _password.value = password
    }
}