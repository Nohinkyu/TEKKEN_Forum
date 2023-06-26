package com.nik.tkforum.ui

import android.content.res.Resources
import android.os.Build
import java.util.*

object SystemConfiguration {

    val currentLocale: Locale
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Resources.getSystem().configuration.locales.get(0)
            } else {
                Resources.getSystem().configuration.locale
            }
        }
}