package com.allens.okdatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.Preferences
import com.allens.okdatastore.data.UserPreferencesSerializer
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
                    .putStringSet("setData", setOf("a", "b", "c", "d"))
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
                okDataStore.getInt("age", 10000)
                    .collect {
                        println("data:$it")
                    }

            }
        })

        viewBinding.linear.addView(createButton("自定义类型写入") {
            runBlocking {
                val dataStore = createOkDataStore(
                    fileName = "user_prefs.pb",
                    serializer = UserPreferencesSerializer
                )

                dataStore.updateData { preferences ->
                    preferences.toBuilder()
                        .setShowCompleted(false)
                        .setAge(22)
                        .setName("江海洋")
                        .setPrice(10.5f)
                        .build()
                }
            }
        })

        viewBinding.linear.addView(createButton("自定义类型读取") {
            runBlocking {
                val dataStore = createOkDataStore(
                    fileName = "user_prefs.pb",
                    serializer = UserPreferencesSerializer
                )
                dataStore.flow()
                    .collect {
                        println("age:${it.age}")
                        println("name:${it.name}")
                        println("price:${it.price}")
                        println("completed:${it.showCompleted}")
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