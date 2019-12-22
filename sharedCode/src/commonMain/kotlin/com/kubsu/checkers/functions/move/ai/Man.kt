package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*

internal fun Board.getAvailableCellsSequence(startCell: Cell.Piece.Man, current: Cell): Sequence<AIMove> =
    increasesSequence
        .map { increase -> getEmptyCellOrNull(startCell.color, current, increase) }
        .filterNotNull()

internal fun Board.getEmptyCellOrNull(color: CellColor, current: Cell, increase: Increase): AIMove? =
    when (val cell = getOrNull(current, increase)) {
        is Cell.Piece -> if (cell.isEnemy(color))
            getOrNull(cell, increase)
                ?.takeIfEmptyOrNull()
                ?.let { AIMove(board = update(cell.toEmpty()), cell = it) }
        else
            null

        is Cell.Empty -> if (!isBackMove(color, increase))
            AIMove(board = this, cell = cell)
        else
            null

        null -> null
    }

internal fun isBackMove(color: CellColor, increase: Increase): Boolean =
    when (color) {
        CellColor.Light -> increase.rowIncrease == 1
        CellColor.Dark -> increase.rowIncrease == -1
    }