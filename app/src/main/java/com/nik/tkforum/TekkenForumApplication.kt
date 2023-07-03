package com.nik.tkforum

import android.app.Application
import com.nik.tkforum.network.AppContainer

class TekkenForumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
    }

    companion object {
        lateinit var appContainer: AppContainer
    }
}