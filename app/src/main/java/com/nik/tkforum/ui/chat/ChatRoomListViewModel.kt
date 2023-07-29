package com.nik.tkforum.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nik.tkforum.data.ChatRoom
import com.nik.tkforum.data.ChatRoomInfo
import com.nik.tkforum.data.User
import com.nik.tkforum.repository.ChatRoomListRepository
import kotlinx.coroutines.launch

class ChatRoomListViewModel(private val repository: ChatRoomListRepository) : ViewModel() {

    private val _chatRoomList = MutableLiveData<List<ChatRoomInfo>>()
    val chatRoomList: LiveData<List<ChatRoomInfo>> = _chatRoomList

    private val _lastChatRoomKey = MutableLiveData<String>()
    val lastChatRoomKey: LiveData<String> = _lastChatRoomKey

    fun loadAllChatRoom() {
        viewModelScope.launch {
            val response = repository.getChatRoomList()
            val chatRoomList = mutableListOf<ChatRoomInfo>()
            val data = response.body()
            data?.let {
                for (chatRoom in data) {
                    chatRoomList.add(ChatRoomInfo(chatRoom.key, chatRoom.value))
                }
            }
            _chatRoomList.value = chatRoomList
        }
    }

    fun createChatRoom(chatRoom: ChatRoom) {
        viewModelScope.launch {
            repository.createChatRoom(chatRoom)
        }
    }

    fun getCreateChatRoomKey() {
        viewModelScope.launch {
            _lastChatRoomKey.value = repository.getCreateChatRoomKey()
        }
    }

    fun joinUser(chatRoomKey: String, user: User) {
        viewModelScope.launch {
            val response = repository.getChatRoomList()
            val data = response.body()
            data?.let {
                if (!data.getValue(chatRoomKey).userList.values.contains(user)) {
                    repository.joinUser(chatRoomKey, user)
                }
            }
        }
    }

    companion object {

        fun provideFactory(repository: ChatRoomListRepository) = viewModelFactory {
            initializer {
                ChatRoomListViewModel(repository)
            }
        }
    }
}
