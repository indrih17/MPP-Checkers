package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.*

/** Game score. */
data class Score(
    val light: Int,
    val dark: Int
)

/** @return the calculated game score. */
fun Board.getScore(): Score {
    val pieces = filterIsInstance<Cell.Piece>()
    return Score(
        light = piecesAmount - pieces.count { it.color is CellColor.Dark },
        dark = piecesAmount - pieces.count { it.color is CellColor.Light }
    )
}

/** @return updated high score for [color]. */
infix fun Score.updateFor(color: CellColor): Score =
    when (color) {
        CellColor.Light -> copy(light = light + 1)
        CellColor.Dark -> copy(dark = dark + 1)
    }