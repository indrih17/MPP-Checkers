package com.kubsu.checkers.functions

import com.kubsu.checkers.data.entities.*

fun createBoard(): Board =
    matrix(size = 8) { row, column ->
        if (isAccessible(row, column))
            when (row) {
                in darkRows -> Cell.Piece.Man(row, column, CellColor.Dark)
                in lightRows -> Cell.Piece.Man(row, column, CellColor.Light)
                else -> Cell.Empty(row, column)
            }
        else
            null
    }

private fun isAccessible(row: Row, column: Column): Boolean =
    (row + column) % 2 != 0

private val darkRows = 0..2
private val lightRows = 5..7
