package com.kubsu.checkers.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kubsu.checkers.R
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.UserState
import com.kubsu.checkers.databinding.ActivityGameBinding
import com.kubsu.checkers.functions.*
import com.kubsu.checkers.render.UiState
import com.kubsu.checkers.startGame
import com.kubsu.checkers.toast

class GameActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) { ActivityGameBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        startGame(
            uiState = UiState(
                tableLayout = binding.boardTableLayout,
                userState = UserState(
                    gameState = createGameState(playerColor = CellColor.Light),
                    startPiece = null
                ),
                scope = lifecycleScope,
                updateData = ::updateData,
                onFail = ::onFailure,
                endGame = ::endGame
            ),
            gameType = intent.getParcelableExtra(gameTypeArg)!!
        )
    }

    override fun onBackPressed() =
        finish()

    private fun onFailure(failure: Failure) {
        binding.messageTextView.text = ""
        toast(
            when (failure) {
                is Failure.IncorrectMove -> getString(R.string.incorrect_move)
                Failure.NoMoves -> getString(R.string.no_moves)
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun updateData(state: GameState) = with(binding) {
        scoreTextView.text = "${state.score.dark}:${state.score.light}"
        messageTextView.setText(
            when (state.activePlayer) {
                CellColor.Light -> R.string.light_moves
                CellColor.Dark -> R.string.dark_moves
            }
        )
    }

    private fun endGame(result: GameResult) = with(binding) {
        messageTextView.setText(
            when (result) {
                GameResult.LightWon -> R.string.light_win
                GameResult.DarkWon -> R.string.dark_win
                GameResult.Draw -> R.string.draw
            }
        )
    }

    companion object {
        const val gameTypeArg = "game_type"
    }
}