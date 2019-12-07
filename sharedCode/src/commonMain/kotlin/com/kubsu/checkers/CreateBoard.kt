package com.kubsu.checkers

import com.kubsu.checkers.data.*

fun createBoard(size: Int): Board =
    matrix(size) { row, column ->
        if (isAccessible(row, column))
            when {
                row.isWhite -> CellType.Piece.White.Man
                row.isBlack -> CellType.Piece.Black.Man
                else -> CellType.Empty
            }
        else
            CellType.Inaccessible
    }

private fun isAccessible(row: Row, column: Column): Boolean =
    (row + column) % 2 != 0

private val Row.isWhite: Boolean
    inline get() = this in 0..2
private val Row.isBlack: Boolean
    inline get() = this in 5..7
