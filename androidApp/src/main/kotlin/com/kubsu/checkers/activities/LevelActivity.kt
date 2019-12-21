package com.kubsu.checkers.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.checkers.GameType
import com.kubsu.checkers.R
import com.kubsu.checkers.startActivity
import kotlinx.android.synthetic.main.activity_level.*

class LevelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        human_vs_human_button.setOnClickListener {
            startGameActivity(GameType.HumanVsHuman)
        }
        ai_vs_ai_button.setOnClickListener {
            startGameActivity(GameType.AiVsAi)
        }
    }

    private fun startGameActivity(gameType: GameType) =
        startActivity<GameActivity> { it.putExtra(GameActivity.gameTypeArg, gameType) }
}