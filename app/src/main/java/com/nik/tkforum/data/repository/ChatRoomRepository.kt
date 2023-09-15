package com.nik.tkforum.data.repository

import com.nik.tkforum.data.model.Chat
import com.nik.tkforum.data.source.remote.network.ApiClient
import com.nik.tkforum.data.source.remote.network.ApiResponse
import javax.inject.Inject

class ChatRoomRepository @Inject constructor(private val apiClient: ApiClient) {

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