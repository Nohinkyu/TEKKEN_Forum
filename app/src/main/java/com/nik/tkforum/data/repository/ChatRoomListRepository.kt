package com.nik.tkforum.data.repository

import com.nik.tkforum.data.model.ChatRoom
import com.nik.tkforum.data.model.User
import com.nik.tkforum.data.source.remote.network.ApiClient
import com.nik.tkforum.data.source.remote.network.ApiResponse
import javax.inject.Inject

class ChatRoomListRepository @Inject constructor(private val apiClient: ApiClient) {

    suspend fun getChatRoomList(auth: String): ApiResponse<Map<String, ChatRoom>> {
        return apiClient.getChatRoomList(auth)
    }

    suspend fun createChatRoom(chatRoom: ChatRoom, auth: String): ApiResponse<Map<String, String>> {
        return apiClient.createChatRoom(chatRoom, auth)
    }

    suspend fun joinUser(
        chatRoomKey: String,
        user: User,
        auth: String
    ): ApiResponse<Map<String, String>> {
        return apiClient.joinUser(chatRoomKey, user, auth)
    }
}