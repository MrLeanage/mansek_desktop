package com.mandiri.most.core.common.utils

interface LocaleManager {
    val appLanguage: AppLanguage
}

enum class AppLanguage(val value: String) {
    EN("en")
}