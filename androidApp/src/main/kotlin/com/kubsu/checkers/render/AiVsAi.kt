package com.kubsu.checkers.render

import android.widget.TableLayout
import com.kubsu.checkers.GameType
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.MoveState
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.GameResult
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.functions.move.ai.makeAIMove

internal fun GameType.AiVsAi.render(
    tableLayout: TableLayout,
    moveState: MoveState,
    updateData: (GameState) -> Unit,
    endGame: (GameResult) -> Unit
) {
    tableLayout.clear()
    val gameState = moveState.gameState
    updateData(gameState)
    val gameResult = gameState.gameResultOrNull()
    if (gameResult != null) {
        endGame(gameResult)
    } else {
        tableLayout.render(gameState.board)
        makeAIMove(gameState).fold(
            ifLeft = { throw IllegalStateException("Incorrect move: fail - $it, \n$gameState\n") },
            ifRight = { render(tableLayout, it, updateData, endGame) }
        )
    }
}

private fun TableLayout.render(board: Board) {
    for (row in board.rows) {
        val tableRow = context.tableRow()

        for (column in board.columns) {
            val current = board.get(row, column)
            val imageView = context.cellImageView(current)
            tableRow.addView(imageView, column)
        }
        addView(tableRow, row)
    }
}