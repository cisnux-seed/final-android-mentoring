package dev.cisnux.dicodingmentoring.ui.chatroom

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.dicodingmentoring.data.realtime.AddChat
import dev.cisnux.dicodingmentoring.data.realtime.Mentee
import dev.cisnux.dicodingmentoring.data.realtime.RealtimeChats
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val roomChatId = checkNotNull(savedStateHandle["roomChatId"]) as String
    private val _showConnectionError = mutableStateOf(false)
    val showConnectionError: State<Boolean> get() = _showConnectionError
    private val _realtimeChats = mutableStateOf<RealtimeChats?>(null)
    val realtimeChats: State<RealtimeChats?> get() = _realtimeChats
    private val _message = mutableStateOf("")
    val message: State<String> get() = _message
    private var _senderId = mutableStateOf<String?>(null)
    private var _receiver = mutableStateOf<Mentee?>(null)
    val receiver: State<Mentee?> get() = _receiver
    val currentUserId: State<String?> get() = _senderId

    fun subscribeRoomChat() = viewModelScope.launch {
        authRepository.currentUser().collectLatest {
            if (it.uid.isNotBlank()) {
                _senderId.value = it.uid
                chatRepository.getRealtimeChats(it.uid, roomChatId)
                    .catch {
                        _showConnectionError.value = true
                    }
                    .collectLatest { chat ->
                        _realtimeChats.value = chat
                        _receiver.value =
                            if (_senderId.value == chat.mentee.id) chat.mentor
                            else chat.mentee
                    }
            }
        }
    }

    fun onMessageChanged(message: String) {
        _message.value = message
    }

    fun onSentNewMessage() = viewModelScope.launch {
        if (_senderId.value != null && _receiver.value != null) {
            val addChat = AddChat(
                roomChatId = roomChatId,
                senderId = _senderId.value!!,
                receiverId = _receiver.value!!.id,
                message = _message.value
            )
            chatRepository.sentMessage(addChat)
            _message.value = ""
        }
    }

    fun closeSocket() = viewModelScope.launch {
        chatRepository.onCloseSocket()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            chatRepository.onCloseSocket()
        }
    }
}