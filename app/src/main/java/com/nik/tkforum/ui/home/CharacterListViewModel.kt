package com.nik.tkforum.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nik.tkforum.data.repository.CharacterListRepository
import com.nik.tkforum.data.repository.UserRepository
import com.nik.tkforum.data.source.local.CharacterListEntity
import com.nik.tkforum.data.source.remote.network.ApiResultSuccess
import com.nik.tkforum.data.source.remote.network.FirebaseData
import com.nik.tkforum.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterListRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _characterList = MutableStateFlow<List<CharacterListSection>>(emptyList())
    val characterList: StateFlow<List<CharacterListSection>> = _characterList

    private val _isCharacterSave: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isCharacterSave: StateFlow<Boolean> = _isCharacterSave

    private val _isSavedFrameData: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isSavedFrameData: StateFlow<Boolean> = _isSavedFrameData

    private val _isLoading: MutableStateFlow<Boolean?> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean?> = _isLoading

    private val _isReDownload: MutableStateFlow<Boolean?> = MutableStateFlow(false)
    val isReDownload: StateFlow<Boolean?> = _isReDownload

    private val _isError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    val isSeriesSwitchCheck = userRepository.checkSeriesData()

    fun loadSevenCharacterList() {
        viewModelScope.launch {
            val sectionList = mutableListOf<CharacterListSection>()
            val characterList = repository.getCharacterList()
            for (sectionInfo in characterList) {
                if (Constants.SEVEN_SEASON_LIST.contains(sectionInfo.season)) {
                    sectionList.add(CharacterListSection.CharacterListHeader(sectionInfo.season))
                    sectionInfo.characterList?.map { character ->
                        sectionList.add(
                            CharacterListSection.Character(
                                character
                            )
                        )
                    }
                }
            }
            _characterList.value = sectionList
        }
    }

    fun loadEightCharacterList() {
        viewModelScope.launch {
            val sectionList = mutableListOf<CharacterListSection>()
            val characterList = repository.getCharacterList()
            for (sectionInfo in characterList) {
                if (Constants.EIGHT_SEASON_LIST.contains(sectionInfo.season)) {
                    sectionList.add(CharacterListSection.CharacterListHeader(sectionInfo.season))
                    sectionInfo.characterList?.map { character ->
                        sectionList.add(
                            CharacterListSection.Character(
                                character
                            )
                        )
                    }
                }
            }
            _characterList.value = sectionList
        }
    }

    fun isSavedFrameData() {
        viewModelScope.launch {
            val data = repository.getCharacterList()
            _isSavedFrameData.value = data.isNotEmpty()
        }
    }

    fun getCharacterList() {
        val auth = FirebaseData.token.toString()
        viewModelScope.launch {
            if (repository.getCharacterList().isEmpty()) {
                _isLoading.value = true
                for (season in Constants.SERIES_LIST) {
                    when (val response = repository.getSeasonCharacterList(season, auth)) {
                        is ApiResultSuccess -> {
                            val entity = CharacterListEntity(
                                season = response.data.season,
                                characterList = response.data.characterList
                            )
                            repository.insertCharacterList(entity)
                        }

                        else -> {
                            _isCharacterSave.value = false
                            _isError.value = true
                        }
                    }
                }
                _isCharacterSave.value = true
                _isSavedFrameData.value = true
                _isError.value = false
            }
            _isLoading.value = false
            _isCharacterSave.value = false
            _isReDownload.value = false
        }
    }

    fun reDownLoadCharacterList() {
        viewModelScope.launch {
            _isReDownload.value = true
            for (season in Constants.SEVEN_SEASON_LIST) {
                repository.deleteCharacterList(season)
            }
            for (season in Constants.EIGHT_SEASON_LIST) {
                repository.deleteCharacterList(season)
            }
            getCharacterList()
            loadSevenCharacterList()
        }
    }
}