package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.UserState
import com.kubsu.checkers.def
import com.kubsu.checkers.functions.update
import com.kubsu.checkers.left
import com.kubsu.checkers.right

suspend fun makeAIMove(gameState: GameState): Either<Failure, UserState> = def {
    gameState
        .getBestMoveOrNull()
        ?.board
        ?.let { Either.right(UserState(gameState = gameState.update(board = it), startCell = null)) }
        ?: Either.left(Failure.NoMoves)
}
