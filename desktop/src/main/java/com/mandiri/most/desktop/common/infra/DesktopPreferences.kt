package com.mandiri.most.desktop.common.infra

import java.util.*
import java.util.prefs.Preferences

class DesktopPreferences {

    companion object {
        const val PREF_PACKAGE_NAME: String = "/com/mandiri"
        const val PREF_SECURE_KEY = "secure_key"
        const val PREF_LANGUAGE_CODE = "language_code"
        const val PREF_COUNTRY_CODE = "country_code"
    }

    val preferences: Preferences = Preferences.userRoot().node(PREF_PACKAGE_NAME)


    fun putSecureKey(key: String) {
        preferences.put(PREF_SECURE_KEY, key)
    }

    fun getSecureKey(): String = preferences.get(PREF_SECURE_KEY, "")

    fun removeSecureKey() {
        preferences.remove(PREF_SECURE_KEY)
    }

    fun getLocale(): Locale {
        val languageCode = preferences.get(PREF_LANGUAGE_CODE, "en")
        val countryCode = preferences.get(PREF_COUNTRY_CODE, "US")
        return Locale(languageCode, countryCode)
    }

    fun setLocale(locale: Locale){
        preferences.put(PREF_LANGUAGE_CODE, locale.language)
        preferences.put(PREF_COUNTRY_CODE, locale.country)
    }
}