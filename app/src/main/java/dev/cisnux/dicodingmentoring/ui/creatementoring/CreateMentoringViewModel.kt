package dev.cisnux.dicodingmentoring.ui.creatementoring

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.cisnux.dicodingmentoring.DicodingMentoringApplication
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.domain.models.AddMentoringSession
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.domain.repositories.MentoringRepository
import dev.cisnux.dicodingmentoring.utils.combineDateAndTime
import dev.cisnux.dicodingmentoring.utils.withDateFormat
import dev.cisnux.dicodingmentoring.utils.withTimeFormat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CreateMentoringViewModel @Inject constructor(
    private val mentoringRepository: MentoringRepository,
    private val authRepository: AuthRepository,
    @ApplicationContext appContext: Context,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(appContext as Application) {
    private val mentorId = checkNotNull(savedStateHandle["id"]) as String
    private var menteeId: String? = null
    private val context = getApplication<DicodingMentoringApplication>()

    private val _title = mutableStateOf("")
    val title: State<String> get() = _title
    private val _description = mutableStateOf("")
    val description: State<String> get() = _description
    private val _mentoringType = mutableStateOf(context.getString(R.string.chat))
    val mentoringType: State<String> get() = _mentoringType
    private var mentoringDateMillis = 0L
    private var mentoringTimeMillis = 0L
    private val _mentoringDate = mutableStateOf("")
    val mentoringDate: State<String> get() = _mentoringDate
    private val _mentoringTime = mutableStateOf("")
    val mentoringTime get() = _mentoringTime
    private val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private val _isCreateMentoringSuccess = mutableStateOf(false)
    val isCreateMentoringSuccess: State<Boolean> get() = _isCreateMentoringSuccess

    init {
        viewModelScope.launch {
            authRepository.currentUser().collectLatest {
                menteeId = it.uid
            }
        }
    }

    fun onMentoringDateChanged(selectedDate: Long?) {
        if (selectedDate != mentoringDateMillis) {
            mentoringTimeMillis = 0L
            _mentoringTime.value = ""
        }
        selectedDate?.let { dateMillis ->
            val selectedCal = Calendar.getInstance()
            selectedCal.time = Date(dateMillis)
            val currentDate = selectedCal.get(Calendar.DAY_OF_MONTH)

            if (currentDate >= today) {
                mentoringDateMillis= dateMillis
                _mentoringDate.value = mentoringDateMillis.withDateFormat()
            }
        }
    }

    fun onMentoringTimeChanged(selectedTime: Long?) {
        selectedTime?.let { timeMillis ->
            if (_mentoringDate.value.isNotBlank()) {
                val selectedCal = Calendar.getInstance()
                selectedCal.time = Date(mentoringDateMillis)
                val currentDate = selectedCal.get(Calendar.DAY_OF_MONTH)

                if (currentDate == today && timeMillis >= System.currentTimeMillis()) {
                    mentoringTimeMillis = timeMillis
                    _mentoringTime.value = mentoringTimeMillis.withTimeFormat()
                } else if (currentDate > today) {
                    mentoringTimeMillis = timeMillis
                    _mentoringTime.value = mentoringTimeMillis.withTimeFormat()
                }
            }
        }
    }

    fun onTitleChanged(title: String) {
        _title.value = title
    }

    fun onDescriptionChanged(description: String) {
        _description.value = description
    }

    fun onMentoringTypeSelected(mentoringType: String) {
        _mentoringType.value = mentoringType
    }

    fun createMentoringSession() = viewModelScope.launch {
        menteeId?.let {
            val addMentoring = AddMentoringSession(
                menteeId = it,
                mentorId = mentorId,
                title = _title.value,
                description = _description.value,
                isOnlyChat = _mentoringType.value == context.getString(R.string.chat),
                eventTime = combineDateAndTime(
                    date = mentoringDateMillis,
                    time = mentoringTimeMillis
                )
            )
            mentoringRepository.createMentoring(addMentoring)
            _isCreateMentoringSuccess.value = true
        }
    }
}