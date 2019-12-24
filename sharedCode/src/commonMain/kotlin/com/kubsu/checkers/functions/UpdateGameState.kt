package com.kubsu.checkers.functions

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.enemy
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.getScore

fun GameState.update(board: Board): GameState {
    val newScore = board.getScore()
    val newPlayer = activePlayerColor.enemy()
    val simpleMoves = if (newScore == score) simpleMoves + 1 else 0
    return GameState(board, newScore, newPlayer, simpleMoves)
}