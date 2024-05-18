package com.ksstats.core.domain.util

interface SavedStateStore<T> {
    fun put(key: String, value: T)
    fun get(key: String): T?
    fun getOrPut(key: String, defaultValue: () -> T): T
}

class MapSavedStateStore<T> : SavedStateStore<T> {
    private val store: MutableMap<String, T> = mutableMapOf<String, T>()
    override fun put(key: String, value: T) {
        store.put(key, value)
    }

    override fun get(key: String): T? = store[key]

    override fun getOrPut(key: String, defaultValue: () -> T): T = store.getOrPut(key, defaultValue)
}