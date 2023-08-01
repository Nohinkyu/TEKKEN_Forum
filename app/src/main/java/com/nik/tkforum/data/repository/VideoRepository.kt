package com.nik.tkforum.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.nik.tkforum.data.converter.VideoListDataSource
import com.nik.tkforum.data.source.remote.network.KakaoApiClient
import javax.inject.Inject

class VideoRepository @Inject constructor(private val apiClient: KakaoApiClient) {

    fun getVideoList(authorization: String, query: String, pageSize: Int) = Pager(
        PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize
        )

    ) {
        VideoListDataSource(
            apiClient,
            query
        )
    }.flow
}