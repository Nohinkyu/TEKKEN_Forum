package com.nik.tkforum.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CharacterListDao {

    @Insert
    suspend fun insertCharacterList(characterListEntity: CharacterListEntity)

    @Query("SELECT * FROM season_character_list_entity")
    suspend fun getAllCharacterList(): List<CharacterListEntity>
}