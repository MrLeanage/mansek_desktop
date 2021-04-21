package com.mandiri.most.desktop.common.infra

import com.mandiri.most.core.auth.management.SecureKeyValueStore

class DesktopSecureKeyValueStore(val preferences: DesktopPreferences) : SecureKeyValueStore {
    //todo (Ray) will add encrypt function
    override fun save(key: String, value: String) {
        preferences.putSecureKey(value)
    }

    override fun load(key: String): String? {
       return preferences.getSecureKey()
    }

    override fun delete(key: String) {
        return preferences.removeSecureKey()
    }
}