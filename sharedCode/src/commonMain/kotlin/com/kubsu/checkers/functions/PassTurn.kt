package com.kubsu.checkers.functions

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.enemy
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.getScore
import com.kubsu.checkers.functions.move.human.calculateSimpleMoves

internal fun GameState.passTurnToEnemy(board: Board): GameState {
    val newScore = board.getScore()
    return GameState(
        board = board,
        score = newScore,
        activePlayer = activePlayer.enemy(),
        simpleMoves = calculateSimpleMoves(newScore)
    )
}
