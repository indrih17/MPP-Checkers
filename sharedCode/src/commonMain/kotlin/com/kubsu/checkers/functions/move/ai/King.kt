package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.entities.increasesSequence

fun Board.getAvailableCellsSequence(startCell: Cell.Piece.King, current: Cell): Sequence<Cell.Empty> =
    increasesSequence()
        .map { increase -> getEmptyCells(startCell, current, increase) }
        .flatten()

private fun Board.getEmptyCells(
    startCell: Cell.Piece.King,
    current: Cell,
    increase: Increase,
    enemyCount: Int = 0
): List<Cell.Empty> =
    when (val cell = getOrNull(current, increase)) {
        is Cell.Piece ->
            if (enemyCount == 0 && cell isEnemy startCell)
                getEmptyCells(startCell, cell, increase, enemyCount + 1)
            else
                emptyList()

        is Cell.Empty ->
            listOf(cell) + getEmptyCells(startCell, cell, increase, enemyCount)

        null -> emptyList()
    }