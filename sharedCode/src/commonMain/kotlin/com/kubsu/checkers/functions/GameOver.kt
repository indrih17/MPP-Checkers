package com.kubsu.checkers.functions

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.entities.filterIsInstance
import com.kubsu.checkers.data.game.GameState

private const val pieceSize = 12
private const val numberOfPiecesForDraw = 3

fun GameState.isGameOver(): Boolean =
    gameResultOrNull() != null

enum class GameResult {
    LightWon,
    DarkWon,
    Draw
}

fun GameState.gameResultOrNull(): GameResult? =
    if (score.light == pieceSize) {
        GameResult.LightWon
    } else if (score.dark == pieceSize) {
        GameResult.DarkWon
    } else {
        val pieces = board.filterIsInstance<Cell.Piece>()
        val lightPieces = pieces.count { it.color == CellColor.Light }
        val darkPieces = pieces.count { it.color == CellColor.Dark }
        if (
            (lightPieces <= numberOfPiecesForDraw || darkPieces <= numberOfPiecesForDraw)
            && movesWithoutAttacks == 15
        )
            GameResult.Draw
        else
            null
    }