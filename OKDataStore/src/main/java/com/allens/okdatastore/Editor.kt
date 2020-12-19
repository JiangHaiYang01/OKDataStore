package com.allens.okdatastore



interface Editor {
    fun putString(key: String, value: String): Editor
    fun putInt(key: String, value: Int): Editor
    fun putLong(key: String, value: Long): Editor
    fun putFloat(key: String, value: Float): Editor
    fun putBoolean(key: String, value: Boolean): Editor
    fun putStringSet(key: String, values: Set<String>): Editor
    suspend fun commit()
}

