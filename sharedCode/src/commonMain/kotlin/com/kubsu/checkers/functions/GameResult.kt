package com.kubsu.checkers.functions

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.getScore

enum class GameResult {
    LightWon, DarkWon, Draw, NoMoves
}

fun GameState.Continues.gameResultOrNull(): GameResult? {
    val score = board.getScore()
    return when (board.piecesAmount) {
        score.light -> GameResult.LightWon
        score.dark -> GameResult.DarkWon
        else -> {
            val survivors: List<Int> = board.survivors()
            return if (
                (survivors.any { it in 1..3 } && simpleMoves > 15)
                || (survivors.any { it in 4..5 } && simpleMoves > 30)
                || (survivors.any { it in 6..7 } && simpleMoves > 60)
            )
                GameResult.Draw
            else
                null
        }
    }
}

private fun Board.survivors(): List<Int> {
    val piecesList = filterIsInstance<Cell.Piece>()
    val light: Int = piecesList.count { it.color == CellColor.Light }
    val dark: Int = piecesList.count { it.color == CellColor.Dark }
    return listOf(light, dark)
}
