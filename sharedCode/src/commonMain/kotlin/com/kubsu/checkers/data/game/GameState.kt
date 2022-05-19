package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.*

/**
 * @param board [Board].
 * @param activePlayer The player currently taking the move.
 * @param simpleMoves The total number of moves without kills.
 */
data class GameState(
    val board: Board,
    val simpleMoves: Int,
    val activePlayer: CellColor
)

internal fun GameState.apply(board: Board): GameState {
    val newScore = board.getScore()
    return GameState(
        board = board,
        activePlayer = activePlayer.enemy,
        simpleMoves = if (newScore == this.board.getScore()) simpleMoves + 1 else 0
    )
}

/** Game score. */
data class Score(
    val light: Int,
    val dark: Int
)

/** @return the calculated game score. */
fun Board.getScore(): Score {
    val pieces = filterIsInstance<Cell.Piece>()
    return Score(
        light = piecesAmount - pieces.count { it.color == CellColor.Dark },
        dark = piecesAmount - pieces.count { it.color == CellColor.Light }
    )
}
