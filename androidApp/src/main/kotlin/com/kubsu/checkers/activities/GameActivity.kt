package com.kubsu.checkers.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.checkers.R
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.UserState
import com.kubsu.checkers.functions.*
import com.kubsu.checkers.render.UiState
import com.kubsu.checkers.startGame
import com.kubsu.checkers.toast
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.coroutines.*

class GameActivity : AppCompatActivity() {
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        startGame(
            uiState = UiState(
                tableLayout = board_table_layout,
                userState = UserState(
                    gameState = createGameState(playerColor = CellColor.Light),
                    startCell = null
                ),
                scope = mainScope,
                updateData = ::updateData,
                onFail = ::onFailure,
                endGame = ::endGame
            ),
            gameType = intent.getParcelableExtra(gameTypeArg)!!
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    override fun onBackPressed() =
        finish()

    private fun onFailure(failure: Failure) =
        toast(
            when (failure) {
                is Failure.IncorrectMove -> "Некорректный ход"
                is Failure.NoMoves -> "Отстутствуют ходы!"
            }
        )

    @SuppressLint("SetTextI18n")
    private fun updateData(state: GameState) {
        score_text_view.text = "${state.score.dark}:${state.score.light}"
        message_text_view.text = when (state.activePlayer.color) {
            CellColor.Light -> "Ходит игрок №1 (Белые)"
            CellColor.Dark -> "Ходит игрок №2 (Чёрные)"
        }
    }

    private fun endGame(result: GameResult) {
        message_text_view.text = when (result) {
            GameResult.LightWon -> "Победа за Белыми! Поздравляем!"
            GameResult.DarkWon -> "Победа за Чёрными! Поздравляем!"
            GameResult.Draw -> "Ничья!"
        }
    }

    companion object {
        const val gameTypeArg = "game_type"
    }
}