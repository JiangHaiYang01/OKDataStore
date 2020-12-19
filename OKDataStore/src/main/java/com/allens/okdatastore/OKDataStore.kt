package com.allens.okdatastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.*

fun Context.createOkDataStore(name: String): OKDataStore {
    return OKDataStoreImpl(name = name, context = this)
}


interface OKDataStore : EditGet {
    //对数据的添加处理
    suspend fun edit(): Editor

    //返回Flow 开发者自行处理
    suspend fun flow(): Flow<Preferences>

}
