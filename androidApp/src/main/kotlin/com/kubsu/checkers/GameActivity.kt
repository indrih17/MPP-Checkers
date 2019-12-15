package com.kubsu.checkers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.functions.cellWasSelected
import com.kubsu.checkers.functions.createBoard
import com.kubsu.checkers.functions.getWinner
import com.kubsu.checkers.functions.isGameOver
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val gameState = GameState(
            board = createBoard(8),
            score = Score(),
            activePlayerColor = CellColor.Light,
            selectedCell = null
        )

        board_table_layout.render(gameState, ::incorrectMove, ::updateData, ::won)
    }

    override fun onBackPressed() =
        finish()

    private fun TableLayout.render(
        gameState: GameState,
        incorrectMove: () -> Unit,
        updateData: (GameState) -> Unit,
        won: (CellColor) -> Unit
    ) {
        clear()
        updateData(gameState)
        if (gameState.score.isGameOver())
            won(gameState.score.getWinner())
        else
            renderBoard(
                board = gameState.board,
                selectedCell = gameState.selectedCell,
                onClick = { selectedCell ->
                    cellWasSelected(
                        gameState = gameState,
                        cell = selectedCell
                    ).fold(
                        ifLeft = { incorrectMove() },
                        ifRight = { render(it, incorrectMove, updateData, won) }
                    )
                }
            )
    }

    private fun TableLayout.renderBoard(
        board: Board,
        selectedCell: Cell.Piece?,
        onClick: (Cell) -> Unit
    ) {
        for (row in board.rows) {
            val tableRow = context.tableRow()

            for (column in board.columns) {
                val current = board.get(row, column)
                val imageView = context.cellImageView(current)
                if (current != null) {
                    imageView.setOnClickListener { onClick(current) }
                    if (current == selectedCell)
                        imageView.setBackgroundColor(Color.GREEN)
                }
                tableRow.addView(imageView, column)
            }
            addView(tableRow, row)
        }
    }

    private fun TableLayout.clear() {
        removeAllViews()
        setBackgroundColor(Color.WHITE)
    }

    private fun Context.tableRow() = TableRow(this).apply {
        isClickable = true
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun Context.cellImageView(cell: Cell?) = ImageView(this).apply {
        setBackgroundColor(Color.BLACK)
        setImageResource(
            when (cell) {
                is Cell.Piece.Man -> cell.res
                is Cell.Piece.King -> cell.res
                is Cell.Empty -> R.drawable.black_cell
                null -> R.drawable.white_cell
            }
        )
    }

    private val Cell.Piece.Man.res: Int
        get() = if (color is CellColor.Light) R.drawable.white_man else R.drawable.black_man

    private val Cell.Piece.King.res: Int
        get() = if (color is CellColor.Light) R.drawable.white_king else R.drawable.black_king

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
    private fun won(color: CellColor) {
        message_text_view.text = when (color) {
            CellColor.Light -> "Победа за Белыми! Поздравляем!"
            CellColor.Dark -> "Победа за Чёрными! Поздравляем!"
        }
    }
}