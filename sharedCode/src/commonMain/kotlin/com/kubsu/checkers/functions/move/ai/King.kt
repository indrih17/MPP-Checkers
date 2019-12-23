package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.entities.increasesSequence

fun Board.getAvailableCellsSequence(current: Cell.Piece.King): Sequence<AIMove> =
    increasesSequence
        .map { increase -> getEmptyCells(current, increase) }
        .flatten()

private fun Board.getEmptyCells(current: Cell.Piece.King, increase: Increase, enemyCount: Int = 0): List<AIMove> =
    when (val cell = getOrNull(current, increase)) {
        is Cell.Piece ->
            if (enemyCount == 0 && cell isEnemy current)
                attackAiMoveOrNull(current, cell, increase)
                    ?.let { listOf(it) + getEmptyCells(it.cell as Cell.Piece.King, increase, enemyCount + 1) }
                    ?: emptyList()
            else
                emptyList()

        is Cell.Empty ->
            aiMove(current = current, new = cell)
                .let { listOf(it) + getEmptyCells(it.cell as Cell.Piece.King, increase, enemyCount) }

        null -> emptyList()
    }