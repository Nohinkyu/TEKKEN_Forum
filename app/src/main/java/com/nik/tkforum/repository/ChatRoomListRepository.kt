package com.nik.tkforum.repository

import com.nik.tkforum.data.ChatRoom
import com.nik.tkforum.data.User
import com.nik.tkforum.network.ApiClient
import com.nik.tkforum.network.ApiResponse

class ChatRoomListRepository(private val apiClient: ApiClient) {

    suspend fun getChatRoomList(): ApiResponse<Map<String, ChatRoom>> {
        return apiClient.getChatRoomList()
    }

    suspend fun createChatRoom(chatRoom: ChatRoom): ApiResponse<Map<String, String>> {
        return apiClient.createChatRoom(chatRoom)
    }

    suspend fun joinUser(chatRoomKey: String, user: User): ApiResponse<Map<String, String>> {
        return apiClient.joinUser(chatRoomKey, user)
    }
}