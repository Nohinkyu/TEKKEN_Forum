package com.nik.tkforum.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharacterListDao {

    @Insert
    suspend fun insertCharacterList(characterListEntity: CharacterListEntity)

    @Query("DELETE FROM season_character_list_entity WHERE season=:season")
    suspend fun delete(season: String)

    @Query("SELECT * FROM season_character_list_entity")
    suspend fun getAllCharacterList(): List<CharacterListEntity>
}