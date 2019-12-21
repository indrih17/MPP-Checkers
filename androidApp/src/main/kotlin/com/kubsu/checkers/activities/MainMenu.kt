package com.kubsu.checkers.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.checkers.R
import com.kubsu.checkers.startActivity
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        button_start.setOnClickListener { startActivity<LevelActivity>() }
    }
}