package com.kubsu.checkers

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun Context.toast(text: String) =
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

inline fun <reified A : AppCompatActivity> Context.startActivity(extra: (Intent) -> Unit = {}) =
    startActivity(Intent(this, A::class.java).also(extra))