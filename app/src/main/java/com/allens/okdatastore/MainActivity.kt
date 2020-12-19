package com.allens.okdatastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.allens.okdatastore.data.UserPreferencesSerializer
import com.allens.okdatastore.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)


        MMKV.initialize(this)

        val okDataStore = createOkDataStore("user")


        viewBinding.linear.addView(createButton("SharePrepare") {
            getSharedPreferences("user", MODE_PRIVATE)
                .edit()
                .putBoolean("bool", true)
                .putString("name", "江海洋")
                .putInt("age", 22)
                .apply()

            println(
                getSharedPreferences("user", MODE_PRIVATE)
                    .getString("name", "empty")
            )
        })


        viewBinding.linear.addView(createButton("迁移") {
            runBlocking {
                createDataStore(
                    name = "user",
                    migrations = listOf(
                        SharedPreferencesMigration(
                            context = this@MainActivity,
                            sharedPreferencesName = "user"
                        )
                    )
                ).data.collect {
                    println("秦怡 $it")
                }
            }

        })

        viewBinding.linear.addView(createButton("检查迁移是否成功") {
            runBlocking {
                createOkDataStore("user")
                    .getString("name", "empty")
                    .collect {
                        println("迁移：$it")
                    }
            }
        })

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


        viewBinding.linear.addView(createButton("MMKV") {
            val defaultMMKV = MMKV.defaultMMKV()
            val time = measureTimeMillis {
//                (1..10000).forEach {
//                    MMKV.defaultMMKV().encode("index$it", "data is $it")
//                }
                (1..10000).forEach {
                    println(defaultMMKV.decodeString("index$it"))
                }
            }
            println("time:$time")


        })

        viewBinding.linear.addView(createButton("DataStore") {
            runBlocking {
                val dataStore: DataStore<Preferences> = createDataStore(
                    name = "test"
                )
                val time = measureTimeMillis {
//                    (1..10000).forEach {
//                        val userName = preferencesKey<String>("index$it")
//                        dataStore.edit { value ->
//                                value[userName] = "data is $it"
//                            }
//                    }
                    dataStore.data
                        .collect {
                            (1..10000).forEach { values ->
                                val data = it[preferencesKey<String>("index$values")]
                                println("data:$data")
                            }
                        }
                }
                println("time:$time")
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