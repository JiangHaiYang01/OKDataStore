package com.allens.okdatastore

import kotlinx.coroutines.flow.Flow

interface EditGet {
    suspend fun getString(key: String, default: String): Flow<String>
    suspend fun getInt(key: String, default: Int): Flow<Int>
    suspend fun getBoolean(key: String, default: Boolean): Flow<Boolean>
    suspend fun getFloat(key: String, default: Float): Flow<Float>
    suspend fun getLong(key: String, default: Long): Flow<Long>
    suspend fun getStringSet(key: String, default: Set<String>): Flow<Set<String>>
}
