package com.nik.tkforum.data.source.remote.network

import com.nik.tkforum.data.model.Chat
import com.nik.tkforum.data.model.ChatRoom
import com.nik.tkforum.data.model.SeasonCharacterList
import com.nik.tkforum.data.model.User
import com.nik.tkforum.data.model.VideoResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET("v2/search/vclip")
    suspend fun getVideo(
        @Header("Authorization") key: String,
        @Query("query") keyword: String,
        @Query("page") page: Int
    ): VideoResponse

    @GET("chatRoomList/{chatRoomKey}/chatList.json")
    suspend fun getChatList(@Path("chatRoomKey") chatRoomKey: String): ApiResponse<Map<String, Chat>>

    @POST("chatRoomList/{chatRoomKey}/chatList.json")
    suspend fun sendChat(
        @Path("chatRoomKey") chatRoomKey: String,
        @Body chat: Chat
    ): ApiResponse<Map<String, String>>

    @POST("chatRoomList/{chatRoomKey}/userList.json")
    suspend fun joinUser(
        @Path("chatRoomKey") chatRoomKey: String,
        @Body user: User
    ): ApiResponse<Map<String, String>>

    @POST("chatRoomList.json")
    suspend fun createChatRoom(@Body chatRoom: ChatRoom): ApiResponse<Map<String, String>>

    @GET("chatRoomList.json")
    suspend fun getChatRoomList(): ApiResponse<Map<String, ChatRoom>>

    @DELETE("chatRoomList/{chatRoomKey}.json")
    suspend fun deleteChatRoom(@Path("chatRoomKey") chatRoomKey: String): ApiResponse<Map<String, String>>

    @GET("8CharacterList.json")
    suspend fun getSeasonCharacterList() : Response<SeasonCharacterList>

    companion object {

        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        fun create(baseUrl: String): ApiClient {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(ApiCallAdapterFactory.create())
                .build()
                .create(ApiClient::class.java)
        }
    }
}