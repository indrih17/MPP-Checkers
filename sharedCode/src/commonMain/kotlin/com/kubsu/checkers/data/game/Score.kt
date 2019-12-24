package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.entities.filterIsInstance

data class Score(
    val light: Int,
    val dark: Int
)

fun Board.getScore(): Score {
    val pieces = filterIsInstance<Cell.Piece>()
    return Score(
        light = 12 - pieces.count { it.color is CellColor.Dark },
        dark = 12 - pieces.count { it.color is CellColor.Light }
    )
}

infix fun Score.updateFor(cell: Cell.Piece) =
    when (cell.color) {
        CellColor.Light -> copy(light = light + 1)
        CellColor.Dark -> copy(dark = dark + 1)
    }