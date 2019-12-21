package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*

internal fun Board.getAvailableCellsSequence(startCell: Cell.Piece.Man, current: Cell): Sequence<Cell.Empty> =
    increasesSequence()
        .map { increase -> getEmptyCellOrNull(startCell.color, current, increase) }
        .filterNotNull()

private fun Board.getEmptyCellOrNull(color: CellColor, current: Cell, increase: Increase): Cell.Empty? {
    val cell = getOrNull(current, increase)
    return if (cell is Cell.Piece && cell.isEnemy(color))
        getOrNull(cell, increase)?.takeIfEmptyOrNull()
    else if (!isBackMove(color, increase))
        cell?.takeIfEmptyOrNull()
    else
        null
}

private fun isBackMove(color: CellColor, increase: Increase): Boolean =
    when (color) {
        CellColor.Light -> increase.rowIncrease == 1
        CellColor.Dark -> increase.rowIncrease == -1
    }