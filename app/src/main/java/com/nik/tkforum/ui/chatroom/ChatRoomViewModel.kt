package com.nik.tkforum.ui.chatroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.data.model.Chat
import com.nik.tkforum.data.model.User
import com.nik.tkforum.data.source.remote.network.ApiResultSuccess
import com.nik.tkforum.data.repository.ChatRoomRepository
import com.nik.tkforum.data.source.local.PreferenceManager
import com.nik.tkforum.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val repository: ChatRoomRepository,
    private val preferenceManager: PreferenceManager
) :
    ViewModel() {

    private val userInfo = User(
        preferenceManager.getString(Constants.KEY_PROFILE_IMAGE, ""),
        preferenceManager.getString(Constants.KEY_NICKNAME, ""),
        preferenceManager.getString(Constants.KEY_MAIL_ADDRESS, "")
    )

    private var chatListener: ChildEventListener? = null

    private val dataBase = FirebaseDatabase.getInstance(BuildConfig.GOOGLE_BASE_URL)

    private val _chatList = MutableLiveData<List<ChatType>>()
    val chatList: LiveData<List<ChatType>> = _chatList

    private val _loadingError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingError: StateFlow<Boolean> = _loadingError

    private val _sendError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val sendError: StateFlow<Boolean> = _sendError

    private val _deleteError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val deleteError: StateFlow<Boolean> = _deleteError

    fun loadChatList(chatRoomKey: String) {
        viewModelScope.launch {
            val response = repository.getChatList(chatRoomKey)
            val chatTypeList = mutableListOf<ChatType>()
            when (response) {
                is ApiResultSuccess -> {
                    for (chat in response.data.values) {
                        if (chat.senderEmail == userInfo.email) {
                            chatTypeList.add(ChatType.SentChat(chat))
                        } else {
                            chatTypeList.add(ChatType.ReceivedChat(chat))
                        }
                    }
                }

                else -> {
                    _loadingError.value = true
                }
            }
            _chatList.value = chatTypeList
        }
    }

    fun sendChat(chatRoomKey: String, message: String) {
        val chat = Chat(userInfo.profileUri, userInfo.nickname, message, userInfo.email)
        viewModelScope.launch {
            when (repository.sendChat(chatRoomKey, chat)) {
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
        val chatTypeList = mutableListOf<ChatType>()
        chatListener = dataBase.getReference("chatRoomList")
            .child(chatRoomKey).child("chatList")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                    val newMessage = dataSnapshot.getValue(Chat::class.java) ?: return
                    if (newMessage.senderEmail == userInfo.email) {
                        chatTypeList.add(ChatType.SentChat(newMessage))
                    } else {
                        chatTypeList.add(ChatType.ReceivedChat(newMessage))
                    }
                    _chatList.value = chatTypeList
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun deleteChatRoom(chatRoomKey: String) {
        viewModelScope.launch {
            when (repository.deleteChatRoom(chatRoomKey)) {
                is ApiResultSuccess -> {
                    _deleteError.value = false
                }

                else -> {
                    _deleteError.value = true
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatListener = null
    }
}