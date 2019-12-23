package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*

internal fun Board.getAvailableCellsSequence(current: Cell.Piece.Man): Sequence<AIMove> =
    increasesSequence
        .map { increase -> getEmptyCellOrNull(current, increase) }
        .filterNotNull()

internal fun Board.getEmptyCellOrNull(current: Cell.Piece.Man, increase: Increase): AIMove? =
    when (val cell = getOrNull(current, increase)) {
        is Cell.Piece -> if (cell isEnemy current)
            attackOrNull(current, cell, increase)
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