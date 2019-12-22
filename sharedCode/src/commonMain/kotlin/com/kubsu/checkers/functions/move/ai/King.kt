package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.entities.increasesSequence

fun Board.getAvailableCellsSequence(startCell: Cell.Piece.King, current: Cell): Sequence<AIMove> =
    increasesSequence
        .map { increase -> getEmptyCells(startCell, current, increase) }
        .flatten()

private fun Board.getEmptyCells(
    startCell: Cell.Piece.King,
    current: Cell,
    increase: Increase,
    enemyCount: Int = 0
): List<AIMove> =
    when (val cell = getOrNull(current, increase)) {
        is Cell.Piece ->
            if (enemyCount == 0 && cell isEnemy startCell)
                update(cell.toEmpty()).getEmptyCells(startCell, cell, increase, enemyCount + 1)
            else
                emptyList()

        is Cell.Empty ->
            listOf(AIMove(this, cell)) + getEmptyCells(startCell, cell, increase, enemyCount)

        null -> emptyList()
    }