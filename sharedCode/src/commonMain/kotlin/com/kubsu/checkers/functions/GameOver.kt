package com.kubsu.checkers.functions

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.entities.filterIsInstance
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.Score

private const val pieceSize = 12
private const val numberOfPiecesForDraw = 3

fun Score.isGameOver(): Boolean =
    light == pieceSize || dark == pieceSize

fun Score.getWinner(): CellColor =
    if (light == pieceSize) CellColor.Light else CellColor.Dark

enum class Result {
    LightWon,
    DarkWon,
    Draw
}

fun GameState.resultOrNull(): Result? =
    if (score.light == pieceSize) {
        Result.LightWon
    } else if (score.dark == pieceSize) {
        Result.DarkWon
    } else {
        val cells = board.filterIsInstance<Cell.Piece>()
        val lightCells = cells.count { it.color == CellColor.Light }
        val darkCells = cells.count { it.color == CellColor.Dark }
        if (
            (lightCells <= numberOfPiecesForDraw || darkCells <= numberOfPiecesForDraw)
            && movesWithoutAttacks == 15
        )
            Result.Draw
        else
            null
    }