package com.nik.tkforum.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nik.tkforum.data.repository.CharacterListRepository
import com.nik.tkforum.data.source.local.CharacterListEntity
import com.nik.tkforum.data.source.remote.network.ApiResultSuccess
import com.nik.tkforum.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterListRepository,
) : ViewModel() {

    private val _characterList = MutableStateFlow<List<CharacterListSection>>(emptyList())
    val characterList: StateFlow<List<CharacterListSection>> = _characterList

    private val _isSevenLoad: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSevenLoad: StateFlow<Boolean> = _isSevenLoad

    private val _isEightLoad: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEightLoad: StateFlow<Boolean> = _isEightLoad

    private val _isCharacterSave: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isCharacterSave: StateFlow<Boolean> = _isCharacterSave

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
            _isSevenLoad.value = true
            _isEightLoad.value = false
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
            _isEightLoad.value = true
            _isSevenLoad.value = false
        }
    }

    fun getCharacterList() {
        viewModelScope.launch {
            for (season in Constants.SERIES_LIST) {
                when (val response = repository.getSeasonCharacterList(season)) {
                    is ApiResultSuccess -> {
                        val entity = CharacterListEntity(
                            season = response.data.season,
                            characterList = response.data.characterList
                        )
                        repository.insertCharacterList(entity)
                    }

                    else -> {
                        _isCharacterSave.value = false
                    }
                }
            }
            _isCharacterSave.value = true
        }
    }
}