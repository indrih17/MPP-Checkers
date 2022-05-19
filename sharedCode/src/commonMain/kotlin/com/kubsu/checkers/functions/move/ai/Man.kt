package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.functions.move.move

internal fun Board.getAvailableCellsSequence(current: Cell.Piece.Man): Sequence<Board> =
    increasesSequence
        .mapNotNull { increase -> getAvailableMoveOrNull(current, increase) }

private fun Board.getAvailableMoveOrNull(current: Cell.Piece.Man, increase: Increase): Board? =
    when (val cell = get(current, increase)) {
        is Cell.Piece -> if (cell isEnemy current)
            attackAiMoveOrNull(current, cell, increase)?.first
        else
            null

        is Cell.Empty -> if (!isBackMove(current.color, increase))
            move(current = current, empty = cell).first
        else
            null

        null -> null
    }

/** @return true if there is an attempt to move back. */
internal fun isBackMove(color: CellColor, increase: Increase): Boolean =
    when (color) {
        CellColor.Light -> increase.rowIncrease == 1
        CellColor.Dark -> increase.rowIncrease == -1
    }