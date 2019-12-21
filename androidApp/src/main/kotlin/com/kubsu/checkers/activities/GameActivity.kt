package com.kubsu.checkers.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.checkers.GameType
import com.kubsu.checkers.R
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.MoveState
import com.kubsu.checkers.functions.*
import com.kubsu.checkers.render.render
import com.kubsu.checkers.toast
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val moveState = MoveState(
            gameState = GameState(boardSize = 8, playerColor = CellColor.Light),
            startCell = null
        )
        when (val gameType = intent.getParcelableExtra<GameType>(gameTypeArg)) {
            is GameType.HumanVsHuman ->
                gameType.render(board_table_layout, moveState, ::incorrectMove, ::updateData, ::endGame)

            is GameType.AiVsAi ->
                gameType.render(board_table_layout, moveState, ::updateData, ::endGame)
        }
    }

    override fun onBackPressed() =
        finish()

    private fun incorrectMove() = toast("Некорректный ход")

    @SuppressLint("SetTextI18n")
    private fun updateData(state: GameState) {
        score_text_view.text = "${state.score.dark}:${state.score.light}"
        message_text_view.text = when (state.activePlayerColor) {
            CellColor.Light -> "Ходит игрок №1 (Белые)"
            CellColor.Dark -> "Ходит игрок №2 (Чёрные)"
        }
    }

    // Показать результат игры
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