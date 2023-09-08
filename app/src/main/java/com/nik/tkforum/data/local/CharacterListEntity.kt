package com.nik.tkforum.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nik.tkforum.data.CharacterData

@Entity(tableName = "season_character_list_entity")
data class CharacterListEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val season: String,
    val characterList: List<CharacterData>,
    )