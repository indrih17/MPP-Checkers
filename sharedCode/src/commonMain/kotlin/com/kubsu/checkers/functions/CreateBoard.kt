package com.kubsu.checkers.functions

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState

const val BOARD_SIZE = 8

fun createGameState(playerColor: CellColor) = GameState.Continues(
    board = createBoard(size = BOARD_SIZE),
    simpleMoves = 0,
    activePlayer = playerColor
)

fun createBoard(size: Int): Board =
    matrix(size) { row, column ->
        if (isAccessible(row, column))
            when (row) {
                in darkRows -> Cell.Piece.Man(row, column, CellColor.Dark)
                in lightRows -> Cell.Piece.Man(row, column, CellColor.Light)
                else -> Cell.Empty(row, column)
            }
        else
            null
    }

internal fun isAccessible(row: Row, column: Column): Boolean =
    (row + column) % 2 != 0

private val darkRows = 0..2
private val lightRows = 5..7
