package com.nik.tkforum.data

data class CharacterData(
    val characterName: String?,
    val characterImage: Int,
    val moveList: List<FrameData>?,
    val throwList: List<FrameData>?,
)