package com.allens.okdatastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import kotlinx.coroutines.flow.Flow

class OKProtoDataStoreImpl<T>(fileName: String, serializer: Serializer<T>, context: Context) {

    private var dataStore = context.createDataStore(fileName, serializer)

    suspend fun updateData(transform: suspend (t: T) -> T): T {
        return dataStore.updateData { transform(it) }
    }

    suspend fun flow(): Flow<T> {
        return dataStore.data
    }
}