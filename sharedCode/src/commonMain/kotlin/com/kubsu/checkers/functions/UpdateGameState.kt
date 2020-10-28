package com.kubsu.checkers.functions

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.enemy
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.getScore
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.data.game.ActivePlayer

fun GameState.update(board: Board): GameState {
    val newScore = board.getScore()
    return GameState(
        board = board,
        score = newScore,
        activePlayer = activePlayer
            .copy(color = activePlayer.color.enemy())
            .updateSimpleMoves(newScore, currentScore = score)
    )
}

fun GameState.updateSimpleMoves(newScore: Score): GameState =
    copy(activePlayer = activePlayer.updateSimpleMoves(newScore, score))

fun ActivePlayer.updateSimpleMoves(newScore: Score, currentScore: Score): ActivePlayer =
    if (newScore == currentScore) copy(simpleMoves = simpleMoves + 1) else copy(simpleMoves = 0)
