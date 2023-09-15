package com.nik.tkforum.data.source.remote.network

import com.nik.tkforum.data.model.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApiClient {

    @GET("v2/search/vclip")
    suspend fun getVideo(
        @Header("Authorization") key: String,
        @Query("query") keyword: String,
        @Query("page") page: Int
    ): VideoResponse
}