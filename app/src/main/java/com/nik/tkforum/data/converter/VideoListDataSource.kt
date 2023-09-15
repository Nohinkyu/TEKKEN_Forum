package com.nik.tkforum.data.converter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.data.model.Video
import com.nik.tkforum.data.source.remote.network.KakaoApiClient
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class VideoListDataSource @Inject constructor(
    private val apiClient: KakaoApiClient,
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