package com.allens.okdatastore

import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.preferencesSetKey
import kotlinx.coroutines.flow.Flow
import java.util.*


interface Editor {
    fun putString(key: String, value: String): Editor
    fun putInt(key: String, value: Int): Editor
    fun putLong(key: String, value: Long): Editor
    fun putFloat(key: String, value: Float): Editor
    fun putBoolean(key: String, value: Boolean): Editor
    fun putStringSet(key: String, values: Set<String>): Editor

    suspend fun commit()
}

interface EditGet {
    suspend fun getString(key: String, default: String): Flow<String>
    suspend fun getInt(key: String, default: Int): Flow<Int>
    suspend fun getBoolean(key: String, default: Boolean): Flow<Boolean>
    suspend fun getFloat(key: String, default: Float): Flow<Float>
    suspend fun getLong(key: String, default: Long): Flow<Long>
    suspend fun getStringSet(key: String, default: Set<String>): Flow<Set<String>>
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

    override fun putStringSet(key: String, values: Set<String>): Editor {
        mModified[key] = values
        return this
    }


    override suspend fun commit() {
        dataStore.edit { dataStore ->
            mModified.forEach {
                println("===========llllll ${it.value::class}")
                println("===========llllll ${it.value::class.java}")
                println("===========llllll ${it.value.javaClass}")
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
                    //todo 这里有一个疑问 为什么不是set
                    LinkedHashSet::class -> {
                        dataStore[preferencesSetKey<String>(it.key)] = setOf((it.value as Set<*>).toString())
                    }
                }
            }
        }
    }
}