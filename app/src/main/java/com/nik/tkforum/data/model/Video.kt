package com.nik.tkforum.data.model

import com.squareup.moshi.Json

data class Video(
    val title: String,
    val url: String,
    val datetime: String,
    @Json(name = "play_time")
    val playTime: Int,
    val thumbnail: String,
    val author: String,
)