package com.nik.tkforum.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nik.tkforum.data.repository.CharacterListRepository
import com.nik.tkforum.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(private val repository: CharacterListRepository) : ViewModel() {

    private val _characterList = MutableStateFlow<List<CharacterListSection>>(emptyList())
    val characterList: StateFlow<List<CharacterListSection>> = _characterList

    private val _isSevenLoad: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSevenLoad: StateFlow<Boolean> = _isSevenLoad

    private val _isEightLoad: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEightLoad: StateFlow<Boolean> = _isEightLoad

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

    companion object {

        fun provideFactory(repository: CharacterListRepository) = viewModelFactory {
            initializer {
                CharacterListViewModel(repository)
            }
        }
    }
}