package com.kubsu.checkers.render

import android.graphics.Color
import android.widget.TableLayout
import com.kubsu.checkers.GameType
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.MoveState
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.functions.move.human.makeMove
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal fun CommonData.updateGame(gameType: GameType.HumanVsHuman) {
    tableLayout.clear()
    updateData(moveState.gameState)
    val gameResult = moveState.gameState.gameResultOrNull()
    if (gameResult != null)
        endGame(gameResult)
    else
        tableLayout.render(
            moveResult = moveState,
            scope = scope,
            onClick = { clickedCell ->
                moveState.makeMove(clickedCell).fold(
                    ifLeft = onFail,
                    ifRight = {
                        copy(moveState = it).updateGame(gameType)
                    }
                )
            }
        )
}

private fun TableLayout.render(
    scope: CoroutineScope,
    moveResult: MoveState,
    onClick: suspend (Cell) -> Unit
) {
    val board = moveResult.gameState.board
    for (row in board.rows) {
        val tableRow = context.tableRow()

        for (column in board.columns) {
            val current = board.get(row, column)
            val imageView = context.cellImageView(current)
            if (current != null) {
                imageView.setOnClickListener {
                    scope.launch(Dispatchers.Main) { onClick(current) }
                }
                if (current == moveResult.startCell)
                    imageView.setBackgroundColor(Color.GREEN)
            }
            tableRow.addView(imageView, column)
        }
        addView(tableRow, row)
    }
}