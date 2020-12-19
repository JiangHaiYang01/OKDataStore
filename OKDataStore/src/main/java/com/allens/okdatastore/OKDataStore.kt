package com.allens.okdatastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun Context.createOkDataStore(name: String): OKDataStore {
    return OKDataStoreImpl(name = name, context = this)
}


interface OKDataStore {
    //对数据的添加处理
    suspend fun edit(): Editor

    //返回Flow 开发者自行处理
    suspend fun flow(): Flow<Preferences>

}

//doEach: () -> Unit, doCatch: (Throwable) -> Unit

class OKDataStoreImpl(name: String, context: Context) : OKDataStore {

    private var dataStore = context.createDataStore(name)

    override suspend fun edit(): Editor {
        return EditorImpl(dataStore)
    }

    override suspend fun flow(): Flow<Preferences> {
        return dataStore.data
    }
}