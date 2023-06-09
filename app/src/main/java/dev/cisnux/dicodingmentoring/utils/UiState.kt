package dev.cisnux.dicodingmentoring.utils

import androidx.compose.runtime.Stable

@Stable
sealed class UiState<out T : Any?> {
    object Initialize : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<out T : Any>(val data: T? = null) : UiState<T>()

    data class Error(val error: Exception?) : UiState<Nothing>()
}