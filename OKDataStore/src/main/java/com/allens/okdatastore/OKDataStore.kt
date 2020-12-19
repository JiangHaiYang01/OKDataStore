package com.allens.okdatastore

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.*

fun Context.createOkDataStore(
    name: String,
    migrations: List<DataMigration<Preferences>> = listOf()
): OKDataStore {
    return OKDataStoreImpl(name = name, context = this, migrations = migrations)
}

fun <T> Context.createOkDataStore(
    fileName: String,
    serializer: Serializer<T>
): OKProtoDataStoreImpl<T> {
    return OKProtoDataStoreImpl(fileName = fileName, serializer = serializer, context = this)
}


interface OKDataStore : EditGet {
    //对数据的添加处理
    suspend fun edit(): Editor

    //返回Flow 开发者自行处理
    suspend fun flow(): Flow<Preferences>

}
