package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.UserState
import com.kubsu.checkers.background
import com.kubsu.checkers.functions.ai.getBestMoveOrNull
import com.kubsu.checkers.functions.move.human.needToMadeKing
import com.kubsu.checkers.functions.update
import com.kubsu.checkers.left
import com.kubsu.checkers.right

suspend fun aIMove(gameState: GameState): Either<Failure, UserState> =
    background { makeAIMove(gameState) }

internal fun makeAIMove(gameState: GameState): Either<Failure, UserState> =
    gameState
        .getBestMoveOrNull()
        ?.board
        ?.let { Either.right(UserState(gameState = gameState.update(board = it), startCell = null)) }
        ?: Either.left(Failure.NoMoves)


internal fun Board.attackAiMoveOrNull(current: Cell.Piece, enemy: Cell.Piece, increase: Increase): AIMove? =
    getOrNull(enemy, increase)
        ?.takeIfEmptyOrNull()
        ?.let { aiMove(current = current, killed = enemy, new = it) }

internal fun Cell.takeIfEmptyOrNull(): Cell.Empty? =
    if (this is Cell.Empty) this else null

data class AIMove(val board: Board, val updatedCell: Cell.Piece)

internal fun Board.aiMove(current: Cell.Piece, killed: Cell.Piece, new: Cell.Empty): AIMove =
    update(killed.toEmpty()).aiMove(current = current, new = new)

internal inline fun <reified T : Cell.Piece> Board.aiMove(current: T, new: Cell.Empty): AIMove {
    val updated = current.updateCoordinates(new)
    val board = swap(current, new)
    return if (updated is Cell.Piece.Man && board.needToMadeKing(updated)) {
        val king = king(updated)
        AIMove(board = board.update(king), updatedCell = king)
    } else {
        AIMove(board = board, updatedCell = updated)
    }
}
