package com.nik.tkforum.repository

import com.nik.tkforum.data.Chat
import com.nik.tkforum.network.ApiClient
import retrofit2.Response

class ChatRoomRepository(private val apiClient: ApiClient) {

    suspend fun getChatList(chatRoomKey: String): Response<Map<String, Chat>> {
        return apiClient.getChatList(chatRoomKey)
    }

    suspend fun sendChat(chatRoomKey: String, chat: Chat): Response<Map<String, String>> {
        return apiClient.sendChat(chatRoomKey, chat)
    }
}