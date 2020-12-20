package com.allens.okdatastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.allens.okdatastore.data.UserPreferencesSerializer
import com.allens.okdatastore.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

    }
}


fun AppCompatActivity.createButton(name: String, action: () -> Unit): MaterialButton {
    return MaterialButton(this).apply {
        text = name
        setOnClickListener {
            action()
        }
    }
}