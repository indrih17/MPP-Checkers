package com.kubsu.checkers.data

data class Score(
    val white: Int,
    val black: Int
)

infix fun Score.updateFor(color: Color) =
    when (color) {
        Color.White -> copy(white = white + 1)
        Color.Black -> copy(black = black + 1)
    }