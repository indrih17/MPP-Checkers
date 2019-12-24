package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.enemy
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.getScore
import com.kubsu.checkers.functions.update
import com.kubsu.checkers.left
import com.kubsu.checkers.right
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun makeAIMove(gameState: GameState): Either<Failure, GameState> =
    withContext(Dispatchers.Default) {
        gameState
            .getBestMoveOrNull()
            ?.board
            ?.let { Either.right(gameState.update(it)) }
            ?: Either.left(Failure.NoMoves)
    }
