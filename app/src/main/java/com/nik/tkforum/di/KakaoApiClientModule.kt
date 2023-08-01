package com.nik.tkforum.di

import com.nik.tkforum.data.source.remote.network.ApiCallAdapterFactory
import com.nik.tkforum.data.source.remote.network.KakaoApiClient
import com.nik.tkforum.util.Constants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KakaoApiClientModule {

    @Singleton
    @Provides
    @Named("KakaoOkHttpClient")
    fun provideApiOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Singleton
    @Provides
    @Named("KakaoRetrofit")
    fun provideRetrofit(
        @Named("KakaoOkHttpClient") okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.KAKAO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiCallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiClient(@Named("KakaoRetrofit") retrofit: Retrofit): KakaoApiClient {
        return retrofit.create(KakaoApiClient::class.java)
    }
}