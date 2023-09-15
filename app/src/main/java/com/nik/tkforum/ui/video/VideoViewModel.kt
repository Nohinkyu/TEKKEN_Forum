package com.nik.tkforum.ui.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.data.model.Video
import com.nik.tkforum.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(private val repository: VideoRepository) : ViewModel() {

    fun loadVideo(keyword: String): Flow<PagingData<Video>> {
        return repository.getVideoList(BuildConfig.KAKAO_API_KEY, keyword, 1)
            .cachedIn(viewModelScope)
    }
}