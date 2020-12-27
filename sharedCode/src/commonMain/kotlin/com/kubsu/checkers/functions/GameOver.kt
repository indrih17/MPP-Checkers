package com.kubsu.checkers.functions

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.entities.filterIsInstance
import com.kubsu.checkers.data.entities.piecesAmount
import com.kubsu.checkers.data.game.GameState

sealed class GameResult {
    object LightWon : GameResult()
    object DarkWon : GameResult()
    object Draw : GameResult()
}

fun GameState.gameResultOrNull(): GameResult? =
    when (board.piecesAmount) {
        score.light -> GameResult.LightWon
        score.dark -> GameResult.DarkWon
        else -> drawOrNull()
    }

internal fun GameState.drawOrNull(): GameResult.Draw? {
    val piecesList = board.filterIsInstance<Cell.Piece>()
    val light: Int = piecesList.count { it.color == CellColor.Light }
    val dark: Int = piecesList.count { it.color == CellColor.Dark }
    val survivors = listOf(light, dark)
    return if (
        (survivors.any { it in 1..3 } && simpleMoves > 30)
        || (survivors.any { it in 4..5 } && simpleMoves > 45)
        || (survivors.any { it in 6..7 } && simpleMoves > 60)
    )
        GameResult.Draw
    else
        null
}