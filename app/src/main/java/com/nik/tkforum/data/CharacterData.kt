package com.nik.tkforum.data

import java.io.Serializable

data class CharacterData(
    val characterName: String?,
    val characterImage: String,
    val moveList: Map<String, FrameData>?,
) : Serializable