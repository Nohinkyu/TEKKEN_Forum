package com.nik.tkforum.data.repository

import com.nik.tkforum.data.source.local.AppDatabase
import com.nik.tkforum.data.source.local.CharacterListEntity

class CharacterListRepository(private val database: AppDatabase) {

    private val dao = database.CharacterListDao()

    suspend fun getCharacterList(): List<CharacterListEntity> {
        return dao.getAllCharacterList()
    }
}
