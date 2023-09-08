package com.nik.tkforum.data.repository

import com.nik.tkforum.data.model.User
import com.nik.tkforum.data.source.local.PreferenceManager
import com.nik.tkforum.util.Constants
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val preferenceManager: PreferenceManager,
) {

    fun getUserInfo() = User(
        preferenceManager.getString(Constants.KEY_PROFILE_IMAGE, ""),
        preferenceManager.getString(Constants.KEY_NICKNAME, ""),
        preferenceManager.getString(Constants.KEY_MAIL_ADDRESS, "")
    )

    fun isChatRoom() = preferenceManager.getString(Constants.KEY_CHAT_ROOM, "")

    fun checkSeriesData() = preferenceManager.getString(Constants.KEY_CHECK_SERIES_DATA, "")

    fun clickSwitch() {
        preferenceManager.putString(Constants.KEY_CHECK_SERIES_DATA, Constants.CLICK_SWITCH)
    }

    fun unCheck() {
        preferenceManager.removeString(Constants.KEY_CHECK_SERIES_DATA)
    }

    fun createChatRoom() {
        preferenceManager.putString(Constants.KEY_CHAT_ROOM, getUserInfo().nickname)
    }

    fun deleteUserInfo() {
        preferenceManager.removeString(Constants.KEY_PROFILE_IMAGE)
        preferenceManager.removeString(Constants.KEY_NICKNAME)
        preferenceManager.removeString(Constants.KEY_MAIL_ADDRESS)
    }

    fun deleteChatRoom() {
        preferenceManager.removeString(Constants.KEY_CHAT_ROOM)
    }
}