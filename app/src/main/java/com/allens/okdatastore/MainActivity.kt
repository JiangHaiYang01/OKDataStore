package com.allens.okdatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.preferences.core.Preferences
import com.allens.okdatastore.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val okDataStore = createOkDataStore("user")

        viewBinding.linear.addView(createButton("add") {
            runBlocking {
                okDataStore
                    .edit()
                    .putString("name", "江海洋")
                    .putInt("age", 21)
                    .putBoolean("isBody", true)
                    .putFloat("size", 20f)
                    .putLong("long", 100L)
                    .commit()

            }
        })
        viewBinding.linear.addView(createButton("Flow") {
            runBlocking {
                okDataStore.flow()
                    .onCompletion { println("complete") }
                    .map { it.asMap() }
                    .collect {
                        it.forEach { map ->
                            println("collect :key:${map.key.name} value:${map.value}")
                        }
                    }
            }
        })
        viewBinding.linear.addView(createButton("Get") {
            runBlocking {
                okDataStore.getLong("long", 10)
                    .collect {
                        println("data:$it")
                    }

            }
        })
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