package dev.cisnux.dicodingmentoring.ui.registerprofile

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.domain.models.AddUserProfile
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.domain.repositories.UserRepository
import dev.cisnux.dicodingmentoring.utils.UiState
import dev.cisnux.dicodingmentoring.utils.isValidAbout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userProfileRepo: UserRepository
) : ViewModel() {
    private val _fullName = mutableStateOf("")
    val fullName: State<String> get() = _fullName
    private val _username = mutableStateOf("")
    val username: State<String> get() = _username
    private val _job = mutableStateOf("")
    private var email: String? = null
    val job: State<String> get() = _job
    private var _about = mutableStateOf("")
    val about: State<String> get() = _about

    private val _createProfileState = MutableStateFlow<UiState<Nothing?>>(UiState.Initialize)
    val createProfileState: StateFlow<UiState<Nothing?>> get() = _createProfileState

    private val _pictureFromGallery = mutableStateOf<Uri?>(null)
    val pictureFromGallery: State<Uri?> get() = _pictureFromGallery

    init {
        viewModelScope.launch {
            authRepository.currentUser().collectLatest {
                email = it.email
            }
        }
    }

    fun setPhotoFromGallery(uri: Uri) {
        _pictureFromGallery.value = uri
    }

    fun onFullNameQueryChanged(fullName: String) {
        _fullName.value = fullName
    }

    fun onUsernameQueryChanged(username: String) {
        _username.value = username
    }

    fun onJobQueryChanged(job: String) {
        _job.value = job
    }

    fun onAboutQueryChanged(about: String, maxAboutLength: Int) {
        if (about.isValidAbout(maxAboutLength))
            _about.value = about
    }

    fun saveAuthSession(id: String, session: Boolean) = viewModelScope.launch {
        authRepository.saveAuthSession(id, session)
    }

    fun onCreateProfile(id: String) = viewModelScope.launch {
        _createProfileState.value = UiState.Loading
        email?.let { userEmail ->
            val addUserProfile = AddUserProfile(
                id = id,
                fullName = fullName.value,
                username = username.value,
                email = userEmail,
                job = job.value,
                photoProfileUri = pictureFromGallery.value,
                about = about.value
            )
            userProfileRepo.addMenteeProfile(addUserProfile).fold(
                { exception ->
                    _createProfileState.value = UiState.Error(exception)
                },
                {
                    _createProfileState.value = UiState.Success()
                }
            )
        }
    }
}