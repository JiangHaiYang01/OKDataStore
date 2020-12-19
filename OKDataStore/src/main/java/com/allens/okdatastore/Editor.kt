package com.allens.okdatastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.Flow
import java.util.*


interface Editor {
    fun putString(key: String, value: String): Editor
    fun putInt(key: String, value: Int): Editor
    fun putLong(key: String, value: Long): Editor
    fun putFloat(key: String, value: Float): Editor
    fun putBoolean(key: String, value: Boolean): Editor

    suspend fun commit()
}

interface EditGet {
    suspend fun getString(key: String, default: String): Flow<String>
    suspend fun getInt(key: String, default: Int): Flow<Int>
    suspend fun getBoolean(key: String, default: Boolean): Flow<Boolean>
    suspend fun getFloat(key: String, default: Float): Flow<Float>
    suspend fun getLong(key: String, default: Long): Flow<Long>
}

class EditorImpl(private val dataStore: DataStore<Preferences>) : Editor {


    private val mModified = HashMap<String, Any>()

    override fun putString(key: String, value: String): Editor {
        mModified[key] = value
        return this
    }

    override fun putInt(key: String, value: Int): Editor {
        mModified[key] = value
        return this
    }

    override fun putLong(key: String, value: Long): Editor {
        mModified[key] = value
        return this
    }

    override fun putFloat(key: String, value: Float): Editor {
        mModified[key] = value
        return this
    }

    override fun putBoolean(key: String, value: Boolean): Editor {
        mModified[key] = value
        return this
    }

    override suspend fun commit() {
        dataStore.edit { dataStore ->
            mModified.forEach {
                println("commit key:${it.key} value:${it.value}")
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