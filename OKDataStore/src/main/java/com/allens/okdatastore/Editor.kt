package com.allens.okdatastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import java.util.*


interface Editor {
    fun putString(key: String, value: String): Editor
    fun putInt(key: String, value: Int): Editor
    fun putLong(key: String, value: Long): Editor
    fun putFloat(key: String, value: Float): Editor
    fun putBoolean(key: String, value: Boolean): Editor

    suspend fun commit()
}

class EditorImpl(private val dataStore: DataStore<Preferences>) : Editor {

    private val mEditorLock = Any()

    private val mModified = HashMap<String, Any>()

    override fun putString(key: String, value: String): Editor {
        synchronized(mEditorLock) {
            mModified[key] = value
            return this
        }
    }

    override fun putInt(key: String, value: Int): Editor {
        synchronized(mEditorLock) {
            mModified[key] = value
            return this
        }
    }

    override fun putLong(key: String, value: Long): Editor {
        synchronized(mEditorLock) {
            mModified[key] = value
            return this
        }
    }

    override fun putFloat(key: String, value: Float): Editor {
        synchronized(mEditorLock) {
            mModified[key] = value
            return this
        }
    }

    override fun putBoolean(key: String, value: Boolean): Editor {
        synchronized(mEditorLock) {
            mModified[key] = value
            return this
        }
    }

    override suspend fun commit() {
        dataStore.edit { dataStore ->
            mModified.forEach {
                when (it.value::class) {
                    Int::class -> {
                        dataStore[preferencesKey<Int>(it.key)] = it.value as Int
                    }
                    String::class -> {
                        dataStore[preferencesKey<String>(it.key)] = it.value as String
                    }
                    Boolean::class -> {
                        dataStore[preferencesKey<Boolean>(it.key)] = it.value as Boolean
                    }
                    Float::class -> {
                        dataStore[preferencesKey<Float>(it.key)] = it.value as Float
                    }
                    Long::class -> {
                        dataStore[preferencesKey<Long>(it.key)] = it.value as Long
                    }
                    Double::class -> {
                        dataStore[preferencesKey<Double>(it.key)] = it.value as Double
                    }
                }
            }
        }
    }
}