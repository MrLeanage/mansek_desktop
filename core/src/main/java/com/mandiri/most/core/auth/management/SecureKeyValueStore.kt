package com.mandiri.most.core.auth.management

interface SecureKeyValueStore {

    fun save(key: String, value: String)

    fun load(key: String): String?

    fun delete(key: String)
}