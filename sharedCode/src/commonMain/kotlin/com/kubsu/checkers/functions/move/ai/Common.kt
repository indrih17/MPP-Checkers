package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.filterIsInstance
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.MoveState
import com.kubsu.checkers.data.minmax.BestMove
import com.kubsu.checkers.functions.ai.minimax
import com.kubsu.checkers.functions.move.human.makeMove
import com.kubsu.checkers.left

fun makeAIMove(gameState: GameState): Either<Failure, MoveState> =
    gameState
        .board
        .filterIsInstance<Cell.Piece>()
        .mapNotNull { gameState.minimax(startCell = it, depth = 4) }
        .maxBy(BestMove::eval)
        ?.let { bestMove ->
            makeMove(
                moveState = MoveState(gameState, startCell = bestMove.startCell as Cell.Piece),
                finishCell = bestMove.finishCell
            )
        }
        ?: Either.left(Failure.NoMoves)

internal fun GameState.getAllMovesSequence(startCell: Cell.Piece, current: Cell): Sequence<Cell> = sequence {

}

