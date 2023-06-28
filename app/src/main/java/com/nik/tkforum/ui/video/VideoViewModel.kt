package com.nik.tkforum.ui.video


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nik.tkforum.BuildConfig
import com.nik.tkforum.data.Video
import com.nik.tkforum.repository.VideoRepository
import kotlinx.coroutines.launch

class VideoViewModel(private val repository: VideoRepository) : ViewModel() {

    private val _videoList = MutableLiveData<List<Video>>()
    val videoList: LiveData<List<Video>> = _videoList

    fun loadVideo(query: String) {
        viewModelScope.launch {
            val videoList = repository.getVideo(BuildConfig.KAKAO_API_KEY, query)
            _videoList.value = videoList
        }
    }

    companion object {

        fun provideFactory(repository: VideoRepository) = viewModelFactory {
            initializer {
                VideoViewModel(repository)
            }
        }
    }
}