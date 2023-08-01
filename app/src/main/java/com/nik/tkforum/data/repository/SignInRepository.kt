package com.nik.tkforum.data.repository

import com.nik.tkforum.data.model.SeasonCharacterList
import com.nik.tkforum.data.source.local.AppDatabase
import com.nik.tkforum.data.source.local.CharacterListEntity
import com.nik.tkforum.data.source.remote.network.ApiClient
import com.nik.tkforum.data.source.remote.network.ApiResponse

class SignInRepository(private val apiClient: ApiClient ,private val database: AppDatabase) {

    private val dao = database.CharacterListDao()

    suspend fun getSeasonCharacterList(season: String): ApiResponse<SeasonCharacterList> {
        return apiClient.getSeasonCharacterList(season)
    }

    suspend fun insertCharacterList(characterListEntity: CharacterListEntity) {
        return dao.insertCharacterList(characterListEntity)
    }
}