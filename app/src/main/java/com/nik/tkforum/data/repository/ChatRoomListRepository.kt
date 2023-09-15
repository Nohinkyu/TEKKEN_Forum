package com.nik.tkforum.data.repository

import com.nik.tkforum.data.model.ChatRoom
import com.nik.tkforum.data.model.User
import com.nik.tkforum.data.source.remote.network.ApiClient
import com.nik.tkforum.data.source.remote.network.ApiResponse

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