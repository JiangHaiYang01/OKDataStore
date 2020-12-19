package com.allens.okdatastore

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.*


class OKDataStoreImpl(
    name: String,
    context: Context,
    migrations: List<DataMigration<Preferences>> = listOf()
) : OKDataStore {

    private var dataStore = context.createDataStore(name = name, migrations = migrations)

    override suspend fun edit(): Editor {
        return EditorImpl(dataStore)
    }

    override suspend fun flow(): Flow<Preferences> {
        return dataStore.data
    }

    /***
     * 创建一个流
     * @param set DataStore 的流 转成set
     * @param key 需要查找的key
     * @return Flow 发射一个流
     */
    private fun requestFlow(
        set: Map<Preferences.Key<*>, Any>,
        key: String
    ): Flow<Pair<Preferences.Key<*>, Any?>> =
        flow {
            var isSame = false
            set.forEach {
                if (key == it.key.name) {
                    isSame = true
                    emit(Pair(it.key, it.value))
                    throw OKDataStoreThrowable()
                }
            }

            if (!isSame) {
                emit(Pair(preferencesKey<String>(key), null))
                throw OKDataStoreThrowable()
            }
        }


    private fun <T> getValueFormKey(key: String, default: T): Flow<T> {
        return dataStore.data
            .map { it.asMap() }
            .flatMapConcat { requestFlow(it, key) }
            .onCompletion { println("complete") }
            .map {
                if (it.second == null) {
                    default
                } else {
                    it.second as T
                }
            }
            .catch { println("发生异常:${it.message}") }
    }


    override suspend fun getString(key: String, default: String): Flow<String> {
        return getValueFormKey(key, default)
    }

    override suspend fun getInt(key: String, default: Int): Flow<Int> {
        return getValueFormKey(key, default)
    }

    override suspend fun getBoolean(key: String, default: Boolean): Flow<Boolean> {
        return getValueFormKey(key, default)
    }

    override suspend fun getFloat(key: String, default: Float): Flow<Float> {
        return getValueFormKey(key, default)
    }

    override suspend fun getLong(key: String, default: Long): Flow<Long> {
        return getValueFormKey(key, default)
    }

    override suspend fun getStringSet(key: String, default: Set<String>): Flow<Set<String>> {
        return getValueFormKey(key, default)
    }
}