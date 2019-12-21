package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.filterIsInstance
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.MoveState
import com.kubsu.checkers.data.minmax.BestMove
import com.kubsu.checkers.functions.ai.minimax
import com.kubsu.checkers.functions.move.human.makeMove
import com.kubsu.checkers.left
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun makeAIMove(gameState: GameState): Either<Failure, MoveState> =
    withContext(Dispatchers.Default) {
        gameState
            .board
            .filterIsInstance<Cell.Piece>()
            .filter { it.color == gameState.activePlayerColor }
            .map { gameState.board.minimax(startCell = it) }
            .maxBy(BestMove::eval)
            ?.let { bestMove ->
                println(bestMove)
                makeMove(
                    moveState = MoveState(gameState, startCell = bestMove.startCell as Cell.Piece),
                    finishCell = bestMove.finishCell
                )
            }
            ?: Either.left(Failure.NoMoves)
    }

internal fun Board.getAllMovesSequence(startCell: Cell.Piece, current: Cell): Sequence<Cell.Empty> =
    when (startCell) {
        is Cell.Piece.Man -> getAvailableCellsSequence(startCell, current)
        is Cell.Piece.King -> getAvailableCellsSequence(startCell, current)
    }

internal fun Cell.takeIfEmptyOrNull(): Cell.Empty? =
    if (this is Cell.Empty) this else null