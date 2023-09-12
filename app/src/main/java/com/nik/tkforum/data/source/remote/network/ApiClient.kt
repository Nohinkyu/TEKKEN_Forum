package com.nik.tkforum.data.source.remote.network

import com.nik.tkforum.data.model.Chat
import com.nik.tkforum.data.model.ChatRoom
import com.nik.tkforum.data.model.SeasonCharacterList
import com.nik.tkforum.data.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET("chatRoomList/{chatRoomKey}/chatList.json")
    suspend fun getChatList(
        @Path("chatRoomKey") chatRoomKey: String,
        @Query("auth") auth: String
    ): ApiResponse<Map<String, Chat>>

    @POST("chatRoomList/{chatRoomKey}/chatList.json")
    suspend fun sendChat(
        @Path("chatRoomKey") chatRoomKey: String,
        @Body chat: Chat,
        @Query("auth") auth: String
    ): ApiResponse<Map<String, String>>

    @POST("chatRoomList/{chatRoomKey}/userList.json")
    suspend fun joinUser(
        @Path("chatRoomKey") chatRoomKey: String,
        @Body user: User,
        @Query("auth") auth: String
    ): ApiResponse<Map<String, String>>

    @POST("chatRoomList.json")
    suspend fun createChatRoom(
        @Body chatRoom: ChatRoom,
        @Query("auth") auth: String
    ): ApiResponse<Map<String, String>>

    @GET("chatRoomList.json")
    suspend fun getChatRoomList(@Query("auth") auth: String): ApiResponse<Map<String, ChatRoom>>

    @DELETE("chatRoomList/{chatRoomKey}.json")
    suspend fun deleteChatRoom(
        @Path("chatRoomKey") chatRoomKey: String,
        @Query("auth") auth: String
    ): ApiResponse<Map<String, String>>

    @GET("{season}.json")
    suspend fun getSeasonCharacterList(
        @Path("season") seasonName: String,
        @Query("auth") auth: String
    ): ApiResponse<SeasonCharacterList>
}