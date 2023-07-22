package com.nik.tkforum.data.converter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.data.Video
import com.nik.tkforum.network.ApiClient
import retrofit2.HttpException
import java.io.IOException

class VideoListDataSource(
    private val apiClient: ApiClient,
    private val query: String
) : PagingSource<Int, Video>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        val start = params.key ?: 1
        return try {
            val data = apiClient.getVideo(BuildConfig.KAKAO_API_KEY, query, start)

            LoadResult.Page(
                data = data.documents,
                prevKey = if (start == 1) null else start - 1,
                nextKey = if (data.documents.isEmpty()) null else start + params.loadSize
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Video>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}