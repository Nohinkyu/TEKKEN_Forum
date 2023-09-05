package com.nik.tkforum.ui.chatroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.TekkenForumApplication
import com.nik.tkforum.data.Chat
import com.nik.tkforum.repository.ChatRoomRepository
import com.nik.tkforum.util.Constants
import kotlinx.coroutines.launch

class ChatRoomViewModel(private val repository: ChatRoomRepository) : ViewModel() {

    private val senderEmail = TekkenForumApplication.preferencesManager.getString(
        Constants.KEY_MAIL_ADDRESS, ""
    )

    private var chatListener: ChildEventListener? = null

    private val dataBase = FirebaseDatabase.getInstance(BuildConfig.GOOGLE_BASE_URL)

    private val _chatList = MutableLiveData<List<ChatType>>()
    val chatList: LiveData<List<ChatType>> = _chatList

    fun loadChatList(chatRoomKey: String) {
        viewModelScope.launch {
            val response = repository.getChatList(chatRoomKey)
            val chatTypeList = mutableListOf<ChatType>()
            val data = response.body()
            data?.let {
                for (chat in data.values) {
                    if (chat.senderEmail == senderEmail) {
                        chatTypeList.add(ChatType.SentChat(chat))
                    } else {
                        chatTypeList.add(ChatType.ReceivedChat(chat))
                    }
                }
            }
            _chatList.value = chatTypeList
        }
    }

    fun sendChat(chatRoomKey: String, chat: Chat) {
        viewModelScope.launch {
            repository.sendChat(chatRoomKey, chat)
        }
    }

    fun addChatListener(chatRoomKey: String) {
        val chatTypeList = mutableListOf<ChatType>()
        chatListener = dataBase.getReference("chatRoomList")
            .child(chatRoomKey).child("chatList")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                    val newMessage = dataSnapshot.getValue(Chat::class.java) ?: return
                    if (newMessage.senderEmail == senderEmail) {
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

    fun deleteChatRoom(chatRoomKey: String){
        viewModelScope.launch {
            repository.deleteChatRoom(chatRoomKey)
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatListener = null
    }

    companion object {

        fun provideFactory(repository: ChatRoomRepository) = viewModelFactory {
            initializer {
                ChatRoomViewModel(repository)
            }
        }
    }
}