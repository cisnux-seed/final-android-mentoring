package dev.cisnux.dicodingmentoring.ui.detailmentoring

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.data.realtime.AcceptMentoring
import dev.cisnux.dicodingmentoring.data.realtime.GetRealtimeDetailMentoring
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.domain.repositories.MentoringRepository
import dev.cisnux.dicodingmentoring.utils.UserType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMentoringViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val mentoringRepository: MentoringRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val mentoringId = checkNotNull(savedStateHandle["mentoringId"]) as String
    private val _showConnectionError = mutableStateOf(false)
    val showConnectionError: State<Boolean> get() = _showConnectionError
    private val _shouldShowLoading = mutableStateOf(true)
    val shouldShowLoading: State<Boolean> get() = _shouldShowLoading
    private val _isAccepted = mutableStateOf(false)
    val isAccepted: State<Boolean> get() = _isAccepted
    val checkBoxDurations = listOf(
        10,
        15,
        20
    )
    private val _userType = mutableStateOf<UserType>(UserType.Mentee)
    val userType: State<UserType> get() = _userType

    private var defaultDuration = checkBoxDurations.first()
    private val _detailMentoring = mutableStateOf<GetRealtimeDetailMentoring?>(null)
    val detailMentoring: State<GetRealtimeDetailMentoring?> get() = _detailMentoring

    fun subscribeDetailMentoring() = viewModelScope.launch {
        authRepository.currentUser().collectLatest {
            Log.d(DetailMentoringViewModel::class.simpleName, it.uid.isNotBlank().toString())
            if (it.uid.isNotBlank()) {
                mentoringRepository.getRealtimeDetailMentoring(it.uid, mentoringId)
                    .catch {
                        _showConnectionError.value = true
                    }
                    .collectLatest { mentoring ->
                        _shouldShowLoading.value = false
                        _detailMentoring.value = mentoring
                        _userType.value =
                            if (it.uid == mentoring.mentee.id) UserType.Mentee else UserType.Mentor
                        _isAccepted.value = mentoring.isAccepted
                    }
            }
        }
    }

    fun onCheckedChanged(duration: Int) {
        defaultDuration = duration
    }

    fun onAccepted(isAccepted: Boolean) {
        _isAccepted.value = isAccepted
    }

    fun onUpdateDetailMentoring() = viewModelScope.launch {
        val acceptMentoring = AcceptMentoring(
            mentoringId = mentoringId,
            duration = defaultDuration,
            isAccepted = _isAccepted.value
        )
        Log.d(DetailMentoringViewModel::class.simpleName, acceptMentoring.toString())
        mentoringRepository.acceptMentoring(
            acceptMentoring
        )
    }

    fun closeSocket() = viewModelScope.launch {
        mentoringRepository.closeDetailMentoringSocket()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            mentoringRepository.closeDetailMentoringSocket()
        }
    }
}