package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.UserState
import com.kubsu.checkers.background
import com.kubsu.checkers.functions.ai.getBestMoveOrNull
import com.kubsu.checkers.functions.passTurnToEnemy
import com.kubsu.checkers.left
import com.kubsu.checkers.right

suspend fun makeAIMove(gameState: GameState): Either<Failure, UserState> =
    background { aiMove(gameState) }

internal fun aiMove(gameState: GameState): Either<Failure, UserState> =
    gameState
        .getBestMoveOrNull()
        ?.board
        ?.let { Either.right(UserState(gameState = gameState.passTurnToEnemy(board = it), startPiece = null)) }
        ?: Either.left(Failure.NoMoves)

/** @return move for artificial intelligence, if possible; otherwise null. */
internal fun Board.attackAiMoveOrNull(current: Cell.Piece, enemy: Cell.Piece, increase: Increase): AIMove? =
    getOrNull(enemy, increase)
        ?.takeIfEmptyOrNull()
        ?.let { aiMove(current = current, killed = enemy, new = it) }

private fun Cell.takeIfEmptyOrNull(): Cell.Empty? =
    if (this is Cell.Empty) this else null

/** Best move for AI: current board and updated piece. */
internal typealias AIMove = Pair<Board, Cell.Piece>

private fun Board.aiMove(current: Cell.Piece, killed: Cell.Piece, new: Cell.Empty): AIMove =
    update(killed.toEmpty()).aiMove(current = current, new = new)

/** @return move for artificial intelligence with updated board and piece. */
internal inline fun <reified T : Cell.Piece> Board.aiMove(current: T, new: Cell.Empty): AIMove {
    val updated = current.updateCoordinates(new)
    val board = swap(current, new)
    return if (updated is Cell.Piece.Man && board.needToMadeKing(updated)) {
        val king = king(updated)
        board.update(king) to king
    } else {
        board to updated
    }
}

/** @return true if a [man] is to be made king. */
fun Board.needToMadeKing(man: Cell.Piece.Man): Boolean =
    man.row == when (man.color) {
        CellColor.Light -> firstIndex
        CellColor.Dark -> lastIndex
    }
