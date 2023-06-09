package dev.cisnux.dicodingmentoring.ui.resetpassword

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.utils.UiState
import dev.cisnux.dicodingmentoring.utils.isEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _resetPasswordState = MutableStateFlow<UiState<Nothing>>(UiState.Loading)
    val resetPasswordState: StateFlow<UiState<Nothing>> get() = _resetPasswordState

    private val _email = mutableStateOf("")
    val email: State<String> get() = _email
    val isValidEmail: State<Boolean> = derivedStateOf {
        if (_email.value.isNotEmpty())
            _email.value.isEmail()
        else true
    }

    fun onEmailQueryChanged(email: String) {
        _email.value = email
    }

    fun resetPassword() = viewModelScope.launch {
        val result = repository.resetPassword(_email.value)
        result.fold(
            {
                _resetPasswordState.value = UiState.Error(it)
            }, {
                _resetPasswordState.value = UiState.Success()
            }
        )
    }
}