package com.kubsu.checkers.render

import android.graphics.Color
import android.widget.TableLayout
import com.kubsu.checkers.GameType
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.MoveState
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.ai.makeAIMove
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.functions.move.human.makeMove
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class ActivePlayer {
    object Human : ActivePlayer() {
        val enemy = Ai
    }
    object Ai : ActivePlayer() {
        val enemy = Human
    }
}

internal fun CommonData.updateGame(
    gameType: GameType.HumanVsAi,
    activePlayer: ActivePlayer
) {
    tableLayout.clear()
    updateData(moveState.gameState)
    moveState
        .gameState
        .gameResultOrNull()
        ?.let(endGame)
        ?: run {
            when (activePlayer) {
                is ActivePlayer.Human ->
                    makeMove(gameType, activePlayer)

                is ActivePlayer.Ai ->
                    makeMove(gameType, activePlayer)
            }
        }
}

internal fun CommonData.makeMove(
    gameType: GameType.HumanVsAi,
    activePlayer: ActivePlayer.Human
) {
    tableLayout.renderHuman(
        moveResult = moveState,
        scope = scope,
        onClick = { clickedCell ->
            moveState.makeMove(clickedCell).fold(
                ifLeft = onFail,
                ifRight = {
                    if (it.startCell != null)
                        copy(moveState = it).updateGame(gameType, activePlayer)
                    else
                        copy(moveState = it).updateGame(gameType, activePlayer.enemy)
                }
            )
        }
    )
}

private fun CommonData.makeMove(
    gameType: GameType.HumanVsAi,
    activePlayer: ActivePlayer.Ai
) {
    val gameState = moveState.gameState
    tableLayout.renderAi(gameState.board)
    scope.launch(Dispatchers.Main) {
        makeAIMove(gameState).fold(
            ifLeft = { throw IllegalStateException("Incorrect move: fail - $it, \n$gameState\n") },
            ifRight = {
                copy(moveState = it).updateGame(gameType, activePlayer.enemy)
            }
        )
    }
}

private fun TableLayout.renderHuman(
    moveResult: MoveState,
    scope: CoroutineScope,
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

private fun TableLayout.renderAi(board: Board) {
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