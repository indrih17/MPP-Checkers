package com.kubsu.checkers.data

data class Score(
    val light: Int = 0,
    val dark: Int = 0
)

infix fun Score.updateFor(cell: Cell.Piece) =
    when (cell.color) {
        CellColor.Light -> copy(light = light + 1)
        CellColor.Dark -> copy(dark = dark + 1)
    }