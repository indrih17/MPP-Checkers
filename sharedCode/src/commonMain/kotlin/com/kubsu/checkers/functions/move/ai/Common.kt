package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*

data class AIMove(val board: Board, val cell: Cell.Piece)

inline fun <reified T : Cell.Piece> Board.aiMove(current: T, new: Cell.Empty): AIMove =
    AIMove(board = swap(current, new), cell = current.updateCoordinates(new))

fun Board.aiMove(current: Cell.Piece, killed: Cell.Piece, new: Cell.Empty): AIMove =
    update(killed.toEmpty()).aiMove(current = current, new = new)

internal fun Board.getAllMovesSequence(startCell: Cell.Piece): Sequence<AIMove> =
    when (startCell) {
        is Cell.Piece.Man -> getAvailableCellsSequence(startCell)
        is Cell.Piece.King -> getAvailableCellsSequence(startCell)
    }

internal fun Cell.takeIfEmptyOrNull(): Cell.Empty? =
    if (this is Cell.Empty) this else null

internal fun Board.attackOrNull(current: Cell.Piece, enemy: Cell.Piece, increase: Increase): AIMove? =
    getOrNull(enemy, increase)
        ?.takeIfEmptyOrNull()
        ?.let { aiMove(current = current, killed = enemy, new = it) }