package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.apply
import com.kubsu.checkers.functions.GameResult
import com.kubsu.checkers.functions.ai.getBestMoveOrNull
import com.kubsu.checkers.functions.move.kill

fun aiMove(gameState: GameState.Continues): GameState =
    gameState
        .getBestMoveOrNull()
        ?.board
        ?.let { gameState.apply(board = it) }
        ?: GameState.GameOver(board = gameState.board, gameResult = GameResult.NoMoves)

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
