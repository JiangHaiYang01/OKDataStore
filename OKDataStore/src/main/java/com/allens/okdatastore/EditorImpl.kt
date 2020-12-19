package com.allens.okdatastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.preferencesSetKey
import java.util.HashMap
import java.util.LinkedHashSet


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