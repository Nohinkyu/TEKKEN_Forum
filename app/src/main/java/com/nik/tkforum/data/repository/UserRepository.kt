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

    fun isChatRoom() = preferenceManager.getString(Constants.KET_CHAT_ROOM, "")

    fun createChatRoom() {
        preferenceManager.putString(Constants.KET_CHAT_ROOM, getUserInfo().nickname)
    }

    fun deleteChatRoom() {
        preferenceManager.removeString(Constants.KET_CHAT_ROOM)
    }
}