package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.management.SecureKeyValueStore

class SimpleMemoryKeyValueStore: SecureKeyValueStore {

    private val data: MutableMap<String, String> = mutableMapOf()

    override fun save(key: String, value: String) {
        data[key] = value
    }

    override fun load(key: String): String? {
        return data[key]
    }

    override fun delete(key: String) {
        data.remove(key)
    }
}