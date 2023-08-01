package com.nik.tkforum.repository

import com.nik.tkforum.data.Chat
import com.nik.tkforum.network.ApiClient
import com.nik.tkforum.network.ApiResponse

class ChatRoomRepository(private val apiClient: ApiClient) {

    suspend fun getChatList(chatRoomKey: String): ApiResponse<Map<String, Chat>> {
        return apiClient.getChatList(chatRoomKey)
    }

    suspend fun sendChat(chatRoomKey: String, chat: Chat): ApiResponse<Map<String, String>> {
        return apiClient.sendChat(chatRoomKey, chat)
    }

    suspend fun deleteChatRoom(chatRoomKey: String): ApiResponse<Map<String, String>> {
        return apiClient.deleteChatRoom(chatRoomKey)
    }
}