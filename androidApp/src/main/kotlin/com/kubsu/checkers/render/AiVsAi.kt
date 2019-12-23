package com.kubsu.checkers.render

import android.widget.TableLayout
import com.kubsu.checkers.GameType
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.ai.makeAIMove
import com.kubsu.checkers.functions.gameResultOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal fun CommonData.updateGame(gameType: GameType.AiVsAi) {
    val gameState = moveState.gameState
    tableLayout.clear()
    updateData(gameState)
    val gameResult = gameState.gameResultOrNull()
    if (gameResult != null) {
        endGame(gameResult)
    } else {
        tableLayout.render(gameState.board)
        scope.launch(Dispatchers.Main) {
            makeAIMove(gameState).fold(
                ifLeft = { throw IllegalStateException("Incorrect move: fail - $it, \n$gameState\n") },
                ifRight = { copy(moveState = it).updateGame(gameType) }
            )
        }
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