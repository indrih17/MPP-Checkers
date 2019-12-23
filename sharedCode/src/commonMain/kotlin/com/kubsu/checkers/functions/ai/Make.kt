package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.filterIsInstance
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.MoveState
import com.kubsu.checkers.functions.move.human.makeMove
import com.kubsu.checkers.left
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun makeAIMove(gameState: GameState): Either<Failure, MoveState> =
    withContext(Dispatchers.Default) {
        gameState
            .getBestMoveOrNull()
            ?.let { bestMove ->
                val moveState = MoveState(gameState, startCell = bestMove.startCell)
                moveState.makeMove(bestMove.finishCell)
            }
            ?: Either.left(Failure.NoMoves)
    }

data class BestMove(
    val startCell: Cell.Piece,
    val finishCell: Cell.Empty,
    val eval: Int
)

internal fun GameState.getBestMoveOrNull(): BestMove? =
    getPlayerPieces()
        .map { board.minimax(current = it) }
        .mapNotNull {
            if (it.finishCell != null)
                BestMove(it.startCell, it.finishCell, it.eval)
            else
                null
        }
        .maxBy(BestMove::eval)

private fun GameState.getPlayerPieces(): ImmutableList<Cell.Piece> =
    board
        .filterIsInstance<Cell.Piece>()
        .filter { it.color == activePlayerColor }
        .toImmutableList()