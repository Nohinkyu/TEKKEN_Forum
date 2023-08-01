package com.nik.tkforum.data.model

import java.io.Serializable

data class CharacterData(
    val characterName: String?,
    val characterImage: String,
    val moveList: Map<String, FrameData>?,
) : Serializable