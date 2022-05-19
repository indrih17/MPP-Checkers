package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.UserState
import com.kubsu.checkers.background
import com.kubsu.checkers.data.game.apply
import com.kubsu.checkers.functions.ai.getBestMoveOrNull
import com.kubsu.checkers.functions.move.kill
import com.kubsu.checkers.left
import com.kubsu.checkers.right

suspend fun makeAIMove(gameState: GameState): Either<Failure, UserState> =
    background { aiMove(gameState) }

internal fun aiMove(gameState: GameState): Either<Failure, UserState> =
    gameState
        .getBestMoveOrNull()
        ?.board
        ?.let { UserState(gameState = gameState.apply(board = it), startPiece = null).right() }
        ?: Failure.NoMoves.left()

/** @return move for artificial intelligence, if possible; otherwise null. */
internal fun Board.attackAiMoveOrNull(current: Cell.Piece.King, enemy: Cell.Piece, increase: Increase): Pair<Board, Cell.Piece.King>? =
    getEmptyCellAfterPieceOrNull(enemy, increase)
        ?.let { kill(current = current, enemy = enemy, empty = it) }

/** @return move for artificial intelligence, if possible; otherwise null. */
internal fun Board.attackAiMoveOrNull(current: Cell.Piece.Man, enemy: Cell.Piece, increase: Increase): Pair<Board, Cell.Piece>? =
    getEmptyCellAfterPieceOrNull(enemy, increase)
        ?.let { kill(current = current, enemy = enemy, empty = it) }

internal fun Board.getEmptyCellAfterPieceOrNull(piece: Cell.Piece, increase: Increase): Cell.Empty? =
    get(piece, increase)?.let { it as? Cell.Empty }
