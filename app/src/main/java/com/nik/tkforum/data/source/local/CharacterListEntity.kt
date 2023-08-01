package com.nik.tkforum.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nik.tkforum.data.model.CharacterData

@Entity(tableName = "season_character_list_entity")
data class CharacterListEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val season: String,
    val characterList: List<CharacterData>,
    )