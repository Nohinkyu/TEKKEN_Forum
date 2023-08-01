package com.nik.tkforum.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nik.tkforum.data.model.ChatRoom
import com.nik.tkforum.data.model.ChatRoomInfo
import com.nik.tkforum.data.model.User
import com.nik.tkforum.data.source.remote.network.ApiResultSuccess
import com.nik.tkforum.data.repository.ChatRoomListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomListViewModel @Inject constructor(
    private val repository: ChatRoomListRepository,
) :
    ViewModel() {

    private val _chatRoomList = MutableStateFlow<List<ChatRoomInfo>>(emptyList())
    val chatRoomList: StateFlow<List<ChatRoomInfo>> = _chatRoomList

    private val _lastChatRoomKey = MutableStateFlow<String>("")
    val lastChatRoomKey: StateFlow<String> = _lastChatRoomKey

    private val _isGetChatRoomKey: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isGetChatRoomKey: StateFlow<Boolean> = _isGetChatRoomKey

    private val _isError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    fun loadAllChatRoom() {
        _isError.value = false
        viewModelScope.launch {
            val response = repository.getChatRoomList()
            val chatRoomList = mutableListOf<ChatRoomInfo>()
            when (response) {
                is ApiResultSuccess -> {
                    _isError.value = false
                    for (chatRoom in response.data) {
                        chatRoomList.add(ChatRoomInfo(chatRoom.key, chatRoom.value))
                    }
                }

                else -> {
                    _isError.value = true
                }
            }
            _chatRoomList.value = chatRoomList
        }
    }

    fun createChatRoom(chatRoom: ChatRoom) {
        viewModelScope.launch {
            when (repository.createChatRoom(chatRoom)) {
                is ApiResultSuccess -> {
                    _isError.value = false
                }

                else -> {
                    _isError.value = true
                }
            }
        }
    }

    fun getCreateChatRoomKey() {
        viewModelScope.launch {
            when (val response = repository.getChatRoomList()) {
                is ApiResultSuccess -> {
                    _isError.value = false
                    _lastChatRoomKey.value = response.data.keys.last()
                    _isGetChatRoomKey.value = true
                }

                else -> {
                    _isError.value = true
                    _isGetChatRoomKey.value = false
                }
            }
        }
    }

    fun joinUser(chatRoomKey: String, user: User) {
        viewModelScope.launch {
            when (val response = repository.getChatRoomList()) {
                is ApiResultSuccess -> {
                    if (!response.data.getValue(chatRoomKey).userList.values.contains(user)) {
                        val result = repository.joinUser(chatRoomKey, user)
                        _isError.value = false
                        when (result) {
                            is ApiResultSuccess -> {
                                _isError.value = false
                            }

                            else -> {
                                _isError.value = true
                            }
                        }
                    }
                }

                else -> {
                    _isError.value = true
                }
            }
        }
    }
}
