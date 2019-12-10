package com.kubsu.checkers.functions

import com.kubsu.checkers.data.*

fun createBoard(size: Int): Board =
    matrix(size) { row, column ->
        if (isAccessible(row, column))
            when (row) {
                in blackRows -> Cell.Piece.Man(row, column, Color.Black)
                in whiteRows -> Cell.Piece.Man(row, column, Color.White)
                else -> Cell.Empty(row, column)
            }
        else
            Cell.Inaccessible(row, column)
    }

private fun isAccessible(row: Row, column: Column): Boolean =
    (row + column) % 2 != 0

private val blackRows = 0..2
private val whiteRows = 5..7
