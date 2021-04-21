package com.mandiri.most.desktop.common

import com.mandiri.most.desktop.common.infra.DesktopPreferences
import java.util.*

class LocaleManager(private val preferences: DesktopPreferences) {

    fun getResourceBundle(): ResourceBundle {
       return ResourceBundle.getBundle("mansek",preferences.getLocale())
    }

    fun setLocal(locale: Locale){
        preferences.setLocale(locale)
    }
}