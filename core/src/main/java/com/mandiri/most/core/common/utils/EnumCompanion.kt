package com.mandiri.most.core.common.utils

open class EnumCompanion<T, V>(private val valueMap: Map<T, V>) {
    fun fromRawValue(type: T) = valueMap[type]
}