package com.nik.tkforum

import android.app.Application
import com.nik.tkforum.data.source.local.PreferenceManager
import com.nik.tkforum.data.source.remote.network.AppContainer

class TekkenForumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
        preferencesManager = PreferenceManager(this)
    }

    companion object {
        lateinit var appContainer: AppContainer
        lateinit var preferencesManager: PreferenceManager
    }
}