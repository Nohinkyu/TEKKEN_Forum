package com.nik.tkforum.data.repository

import com.nik.tkforum.data.model.SeasonCharacterList
import com.nik.tkforum.data.source.local.AppDatabase
import com.nik.tkforum.data.source.local.CharacterListEntity
import com.nik.tkforum.data.source.remote.network.ApiClient
import com.nik.tkforum.data.source.remote.network.ApiResponse
import javax.inject.Inject

class CharacterListRepository @Inject constructor(
    private val apiClient: ApiClient,
    private val database: AppDatabase
) {

    private val dao = database.CharacterListDao()

    suspend fun getCharacterList(): List<CharacterListEntity> {
        return dao.getAllCharacterList()
    }

    suspend fun getSeasonCharacterList(season: String): ApiResponse<SeasonCharacterList> {
        return apiClient.getSeasonCharacterList(season)
    }

    suspend fun insertCharacterList(characterListEntity: CharacterListEntity) {
        return dao.insertCharacterList(characterListEntity)
    }

    suspend fun deleteCharacterList(season: String) {
        dao.delete(season)
    }
}
