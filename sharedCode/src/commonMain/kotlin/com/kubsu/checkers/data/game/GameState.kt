package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.CellColor

/**
 * @param board [Board].
 * @param score [Score].
 * @param activePlayer The player currently taking the move.
 * @param simpleMoves The total number of moves without kills.
 */
data class GameState(
    val board: Board,
    val score: Score,
    val simpleMoves: Int,
    val activePlayer: CellColor
)
