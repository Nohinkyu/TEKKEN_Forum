package com.nik.tkforum.ui.chatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ChildEventListener
import com.nik.tkforum.data.model.Chat
import com.nik.tkforum.data.source.remote.network.ApiResultSuccess
import com.nik.tkforum.data.repository.ChatRoomRepository
import com.nik.tkforum.data.repository.UserRepository
import com.nik.tkforum.data.source.remote.network.FirebaseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val repository: ChatRoomRepository,
    private val userRepository: UserRepository,
) :
    ViewModel() {

    private var chatListener: ChildEventListener? = null

    private val _chatList = MutableStateFlow<List<Chat>>(emptyList())
    val chatList: StateFlow<List<Chat>> = _chatList

    private val _isLoading: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isLoading: StateFlow<Boolean?> = _isLoading

    private val _sendError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val sendError: StateFlow<Boolean> = _sendError

    private val _deleteError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val deleteError: StateFlow<Boolean> = _deleteError

    private val _isChatRoomHost: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isChatRoomHost: StateFlow<Boolean> = _isChatRoomHost

    private val _isSuccessDeleteChatRoom: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSuccessDeleteChatRoom: StateFlow<Boolean> = _isSuccessDeleteChatRoom

    val userInfo = userRepository.getUserInfo()

    private val auth = FirebaseData.token.toString()

    fun loadChatList(chatRoomKey: String) {
        viewModelScope.launch {
            repository.getChatList(
                onComplete = { _isLoading.value = true },
                onError = { code, message ->
                    _isLoading.value = code == 200
                },
                chatRoomKey,
                auth
            ).collect { }
        }
    }

    fun sendChat(chatRoomKey: String, message: String) {
        val chat = Chat(userInfo.profileUri, userInfo.nickname, message, userInfo.email)
        viewModelScope.launch {
            when (repository.sendChat(chatRoomKey, chat, auth)) {
                is ApiResultSuccess -> {
                    _sendError.value = false
                }

                else -> {
                    _sendError.value = true
                }
            }
        }
    }

    fun addChatListener(chatRoomKey: String) {
        viewModelScope.launch {
            chatListener = repository.addChatListener(chatRoomKey) { chat ->
                val currentList = _chatList.value
                val newList = currentList.toMutableList().apply { add(chat) }
                _chatList.value = newList
            }
        }
    }

    fun deleteChatRoom(chatRoomKey: String, creatorUserName: String) {
        if (creatorUserName == userInfo.nickname) {
            _isChatRoomHost.value = false
            userRepository.deleteChatRoom()
            viewModelScope.launch {
                when (repository.deleteChatRoom(chatRoomKey, auth)) {
                    is ApiResultSuccess -> {
                        _deleteError.value = false
                        _isSuccessDeleteChatRoom.value = true
                    }

                    else -> {
                        _deleteError.value = true
                        _isSuccessDeleteChatRoom.value = false
                    }
                }
            }
        } else {
            _isChatRoomHost.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatListener = null
    }
}