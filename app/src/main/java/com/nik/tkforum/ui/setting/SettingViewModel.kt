package com.nik.tkforum.ui.setting

import androidx.lifecycle.ViewModel
import com.nik.tkforum.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    val userInfo = repository.getUserInfo()

    val isSwitchCheck = repository.checkSeriesData()

    fun deleteUserInfo() {
        repository.deleteUserInfo()
    }

    fun clickSwitch() {
        repository.clickSwitch()
    }

    fun unCheckSwitch() {
        repository.unCheck()
    }
}