package com.kubsu.checkers.render

import android.graphics.Color
import android.widget.TableLayout
import com.kubsu.checkers.GameType
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.MoveState
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.GameResult
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.functions.move.human.makeMove
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal suspend fun GameType.HumanVsHuman.render(
    tableLayout: TableLayout,
    mainScope: CoroutineScope,
    moveResult: MoveState,
    updateData: (GameState) -> Unit,
    onFail: (Failure.IncorrectMove) -> Unit,
    endGame: (GameResult) -> Unit
) {
    tableLayout.clear()
    updateData(moveResult.gameState)
    val gameResult = moveResult.gameState.gameResultOrNull()
    if (gameResult != null)
        endGame(gameResult)
    else
        tableLayout.render(
            moveResult = moveResult,
            mainScope = mainScope,
            onClick = { clickedCell ->
                makeMove(moveResult, clickedCell).fold(
                    ifLeft = onFail,
                    ifRight = {
                        render(tableLayout, mainScope, it, updateData, onFail, endGame)
                    }
                )
            }
        )
}

private inline fun TableLayout.render(
    moveResult: MoveState,
    mainScope: CoroutineScope,
    crossinline onClick: suspend (Cell) -> Unit
) {
    val board = moveResult.gameState.board
    for (row in board.rows) {
        val tableRow = context.tableRow()

        for (column in board.columns) {
            val current = board.get(row, column)
            val imageView = context.cellImageView(current)
            if (current != null) {
                imageView.setOnClickListener { mainScope.launch { onClick(current) } }
                if (current == moveResult.startCell)
                    imageView.setBackgroundColor(Color.GREEN)
            }
            tableRow.addView(imageView, column)
        }
        addView(tableRow, row)
    }
}