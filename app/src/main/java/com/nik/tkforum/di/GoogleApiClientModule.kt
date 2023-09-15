package com.nik.tkforum.di

import com.nik.tkforum.BuildConfig
import com.nik.tkforum.data.source.remote.network.ApiCallAdapterFactory
import com.nik.tkforum.data.source.remote.network.ApiClient
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
object GoogleApiClientModule {

    @Singleton
    @Provides
    @Named("GoogleOkHttpClient")
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
    @Named("GoogleApiRetrofit")
    fun provideRetrofit(
        @Named("GoogleOkHttpClient") okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.GOOGLE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiCallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiClient(@Named("GoogleApiRetrofit") retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }
}