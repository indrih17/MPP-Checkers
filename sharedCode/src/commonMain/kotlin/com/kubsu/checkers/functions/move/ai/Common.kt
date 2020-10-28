package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.minmax.MaximizingPlayer
import com.kubsu.checkers.data.minmax.isSameColor
import com.kubsu.checkers.functions.move.human.needToMadeKing

internal fun Board.getAllMovesSequence(player: MaximizingPlayer): Sequence<Board> =
    filterIsInstance<Cell.Piece>()
        .asSequence()
        .filter(player::isSameColor)
        .map { startCell ->
            when (startCell) {
                is Cell.Piece.Man -> getAvailableCellsSequence(startCell)
                is Cell.Piece.King -> getAvailableCellsSequence(startCell)
            }
        }
        .flatten()

internal fun Board.attackAiMoveOrNull(current: Cell.Piece, enemy: Cell.Piece, increase: Increase): AIMove? =
    getOrNull(enemy, increase)
        ?.takeIfEmptyOrNull()
        ?.let { aiMove(current = current, killed = enemy, new = it) }

internal fun Cell.takeIfEmptyOrNull(): Cell.Empty? =
    if (this is Cell.Empty) this else null

data class AIMove(val board: Board, val updatedCell: Cell.Piece)

inline fun <reified T : Cell.Piece> Board.aiMove(current: T, new: Cell.Empty): AIMove {
    val updated = current.updateCoordinates(new)
    val board = swap(current, new)
    return if (updated is Cell.Piece.Man && board.needToMadeKing(updated)) {
        val king = king(updated)
        AIMove(board = board.update(king), updatedCell = king)
    } else {
        AIMove(board = board, updatedCell = updated)
    }
}

fun Board.aiMove(current: Cell.Piece, killed: Cell.Piece, new: Cell.Empty): AIMove =
    update(killed.toEmpty()).aiMove(current = current, new = new)
