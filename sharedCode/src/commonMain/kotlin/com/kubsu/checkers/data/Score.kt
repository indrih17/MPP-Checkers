package com.kubsu.checkers.data

data class Score(
    val white: Int = 0,
    val black: Int = 0
)

infix fun Score.updateFor(cell: Cell.Piece) =
    when (cell.color) {
        CellColor.White -> copy(white = white + 1)
        CellColor.Black -> copy(black = black + 1)
    }