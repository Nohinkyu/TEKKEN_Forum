package com.nik.tkforum.data.repository

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.data.model.Chat
import com.nik.tkforum.data.source.remote.network.ApiClient
import com.nik.tkforum.data.source.remote.network.ApiResponse
import com.nik.tkforum.data.source.remote.network.ApiResultSuccess
import com.nik.tkforum.data.source.remote.network.onError
import com.nik.tkforum.data.source.remote.network.onException
import com.nik.tkforum.data.source.remote.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class ChatRoomRepository @Inject constructor(private val apiClient: ApiClient) {

    fun getChatList(
        onComplete: () -> Unit,
        onError: (code: Int, message: String?) -> Unit,
        chatRoomKey: String
    ): Flow<ApiResponse<Map<String, Chat>>> = flow {
        val response = apiClient.getChatList(chatRoomKey)
        response.onSuccess { data ->
            emit(ApiResultSuccess(data))
        }.onError { code, message ->
            onError(code, "code: $code, message: $message")
        }.onException {
            onError(-1, it.message)
        }
    }.onCompletion {
        onComplete()
    }.flowOn(Dispatchers.Default)

    suspend fun sendChat(chatRoomKey: String, chat: Chat): ApiResponse<Map<String, String>> {
        return apiClient.sendChat(chatRoomKey, chat)
    }

    suspend fun deleteChatRoom(chatRoomKey: String): ApiResponse<Map<String, String>> {
        return apiClient.deleteChatRoom(chatRoomKey)
    }

    fun addChatListener(
        chatRoomKey: String,
        onChatItemAdded: (Chat) -> Unit
    ): ChildEventListener {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                val newMessage = dataSnapshot.getValue(Chat::class.java) ?: return
                newMessage?.let { onChatItemAdded(it) }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
            }
        }

        val database =
            FirebaseDatabase.getInstance(BuildConfig.GOOGLE_BASE_URL).getReference("chatRoomList")
                .child(chatRoomKey).child("chatList").addChildEventListener(childEventListener)
        return childEventListener
    }
}