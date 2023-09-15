package com.nik.tkforum.data.source.remote.network

import com.nik.tkforum.BuildConfig
import com.nik.tkforum.util.Constants

class AppContainer {

    private var apiClient: ApiClient? = null

    fun provideKakaoApiClient(): ApiClient {
        return apiClient ?: ApiClient.create(Constants.KAKAO_BASE_URL).apply {
            apiClient = this
        }
    }

    fun provideGoogleApiClient(): ApiClient {
        return apiClient ?: ApiClient.create(BuildConfig.GOOGLE_BASE_URL).apply {
            apiClient = this
        }
    }
}