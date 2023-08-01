package com.nik.tkforum.data.model

import com.nik.tkforum.data.model.CharacterData
import java.io.Serializable

data class SeasonCharacterList(
    val season: String,
    val characterList: List<CharacterData>?
) : Serializable
