package com.nik.tkforum.data

import java.io.Serializable

data class SeasonCharacterList(
    val season: String,
    val characterList: List<CharacterData>?
) : Serializable
