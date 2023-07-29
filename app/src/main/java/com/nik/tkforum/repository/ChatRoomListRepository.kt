package com.nik.tkforum.repository

import com.nik.tkforum.data.ChatRoom
import com.nik.tkforum.data.User
import com.nik.tkforum.network.ApiClient
import retrofit2.Response

class ChatRoomListRepository(private val apiClient: ApiClient) {

    suspend fun getChatRoomList(): Response<Map<String, ChatRoom>> {
        return apiClient.getChatRoomList()
    }

    suspend fun createChatRoom(chatRoom: ChatRoom): Response<Map<String, String>> {
        return apiClient.createChatRoom(chatRoom)
    }

    suspend fun getCreateChatRoomKey(): String? {
        return apiClient.getChatRoomList().body()?.keys?.last()
    }

    suspend fun joinUser(chatRoomKey: String, user: User): Response<Map<String, String>> {
        return apiClient.joinUser(chatRoomKey, user)
    }
}