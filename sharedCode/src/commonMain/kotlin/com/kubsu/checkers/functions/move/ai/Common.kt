package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.Cell

data class AIMove(val board: Board, val cell: Cell.Empty)

internal fun Board.getAllMovesSequence(startCell: Cell.Piece, current: Cell): Sequence<AIMove> =
    when (startCell) {
        is Cell.Piece.Man -> getAvailableCellsSequence(startCell, current)
        is Cell.Piece.King -> getAvailableCellsSequence(startCell, current)
    }

internal fun Cell.takeIfEmptyOrNull(): Cell.Empty? =
    if (this is Cell.Empty) this else null