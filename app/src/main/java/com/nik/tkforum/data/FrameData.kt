package com.nik.tkforum.data

data class FrameData(
    val name: String,
    val numberCommand: String,
    val arrowCommand: String,
    val startUpFrame: String,
    val gardFrame: String,
    val hitFrame: String,
    val counterFrame: String,
    val hitLevel: String,
    val damage: String,
    val note: String = "",
)