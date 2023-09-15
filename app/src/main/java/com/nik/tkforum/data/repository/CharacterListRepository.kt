package com.nik.tkforum.data.repository

import com.nik.tkforum.data.source.local.AppDatabase
import com.nik.tkforum.data.source.local.CharacterListEntity
import javax.inject.Inject

class CharacterListRepository @Inject constructor(private val database: AppDatabase) {

    private val dao = database.CharacterListDao()

    suspend fun getCharacterList(): List<CharacterListEntity> {
        return dao.getAllCharacterList()
    }
}
