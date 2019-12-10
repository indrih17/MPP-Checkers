package com.kubsu.checkers.data

data class Score(
    val white: Int,
    val black: Int
)

infix fun Score.updateFor(cell: Cell.Piece) =
    when (cell.color) {
        Color.White -> copy(white = white + 1)
        Color.Black -> copy(black = black + 1)
    }