package com.kubsu.checkers.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.checkers.GameType
import com.kubsu.checkers.databinding.ActivityLevelBinding
import com.kubsu.checkers.startActivity

class LevelActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) { ActivityLevelBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            setContentView(root)
            humanVsHumanButton.setOnClickListener {
                startGameActivity(GameType.HumanVsHuman)
            }
            humanVsAiButton.setOnClickListener {
                startGameActivity(GameType.HumanVsAi)
            }
            aiVsAiButton.setOnClickListener {
                startGameActivity(GameType.AiVsAi)
            }
        }
    }

    private fun startGameActivity(gameType: GameType) =
        startActivity<GameActivity> { it.putExtra(GameActivity.gameTypeArg, gameType) }
}