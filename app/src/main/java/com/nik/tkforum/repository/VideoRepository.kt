package com.nik.tkforum.repository

import com.nik.tkforum.data.Video
import com.nik.tkforum.network.ApiClient

class VideoRepository(private val apiClient: ApiClient) {

    suspend fun getVideo(authorization: String, query: String): List<Video> {
        return apiClient.getVideo(authorization, query).documents
    }
}