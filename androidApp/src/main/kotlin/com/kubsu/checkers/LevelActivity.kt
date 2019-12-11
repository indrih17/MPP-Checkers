package com.kubsu.checkers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_level.*

class LevelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        button_start_one_easy.setOnClickListener {
            startActivity<GameActivity> { intent ->
                intent.putExtra("count_players", 1)
                intent.putExtra("level", "easy")
            }
        }

        button_start_one_hard.setOnClickListener {
            startActivity<GameActivity> { intent ->
                intent.putExtra("count_players", 1)
                intent.putExtra("level", "hard")
            }
        }

        button_start_two.setOnClickListener {
            startActivity<GameActivity> { intent ->
                intent.putExtra("count_players", 2)
                intent.putExtra("level", "easy")
            }
        }
    }
}