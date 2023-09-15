package com.nik.tkforum.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.nik.tkforum.data.converter.VideoListDataSource
import com.nik.tkforum.data.source.remote.network.ApiClient

class VideoRepository(private val apiClient: ApiClient) {

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