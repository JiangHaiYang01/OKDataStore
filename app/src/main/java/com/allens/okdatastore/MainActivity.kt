package com.allens.okdatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.allens.okdatastore.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val okDataStore = createOkDataStore("user")

        viewBinding.linear.addView(createButton("add") {
            GlobalScope.launch {
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
            GlobalScope.launch {
                okDataStore
                    .flow()
                    .onEach { pref ->
                        pref.asMap().forEach { map ->
                            println("key:${map.key.name} value:${map.value}")
                        }
                    }
                    .catch { catch ->
                        println("catch:${catch.message}")
                    }
                    .collect()
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