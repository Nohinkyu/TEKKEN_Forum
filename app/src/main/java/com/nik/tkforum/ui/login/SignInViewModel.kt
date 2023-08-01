package com.nik.tkforum.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nik.tkforum.data.repository.SignInRepository
import com.nik.tkforum.data.source.local.CharacterListEntity
import com.nik.tkforum.data.source.remote.network.ApiResultSuccess
import com.nik.tkforum.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: SignInRepository):ViewModel() {

    private val _isSave: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSave: StateFlow<Boolean> = _isSave

    fun getSevenCharacterList() {
        viewModelScope.launch {
            for (season in Constants.SEVEN_SEASON_LIST) {
                when (val response = repository.getSeasonCharacterList(season)) {
                    is ApiResultSuccess -> {
                        val entity = CharacterListEntity(
                            season = response.data.season,
                            characterList = response.data.characterList
                        )
                        repository.insertCharacterList(entity)
                    }

                    else -> {
                        _isSave.value = false
                    }
                }
            }
        }
    }

    fun getEightCharacterList(){
        viewModelScope.launch {
            for (season in Constants.EIGHT_SEASON_LIST) {
                when (val response = repository.getSeasonCharacterList(season)) {
                    is ApiResultSuccess -> {
                        val entity = CharacterListEntity(
                            season = response.data.season,
                            characterList = response.data.characterList
                        )
                        repository.insertCharacterList(entity)
                    }

                    else -> {
                        _isSave.value = false
                    }
                }
            }
        }
    }

    companion object {

        fun provideFactory(repository: SignInRepository) = viewModelFactory {
            initializer {
                SignInViewModel(repository)
            }
        }
    }
}