package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*

internal fun Board.getAvailableCellsSequence(current: Cell.Piece.Man): Sequence<Board> =
    increasesSequence
        .mapNotNull { increase -> getAvailableCells(current, increase)?.board }

internal fun Board.getAvailableCells(current: Cell.Piece.Man, increase: Increase): AIMove? =
    when (val cell = getOrNull(current, increase)) {
        is Cell.Piece -> if (cell isEnemy current)
            attackAiMoveOrNull(current, cell, increase)
        else
            null

        is Cell.Empty -> if (!isBackMove(current.color, increase))
            aiMove(current = current, new = cell)
        else
            null

        null -> null
    }

internal fun isBackMove(color: CellColor, increase: Increase): Boolean =
    when (color) {
        CellColor.Light -> increase.rowIncrease == 1
        CellColor.Dark -> increase.rowIncrease == -1
    }