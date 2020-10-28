package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.entities.increasesSequence

internal fun Board.getAvailableCellsSequence(current: Cell.Piece.King): Sequence<Board> =
    increasesSequence
        .map { increase -> getAvailableCells(current, increase).map(AIMove::board) }
        .flatten()

private fun Board.getAvailableCells(
    current: Cell.Piece.King,
    increase: Increase,
    enemyCount: Int = 0
): List<AIMove> =
    when (val cell = getOrNull(current, increase)) {
        is Cell.Piece ->
            if (enemyCount == 0 && cell isEnemy current)
                attack(current, cell, increase, enemyCount)
            else
                emptyList()

        is Cell.Empty ->
            simpleMove(current, cell, increase, enemyCount)

        null -> emptyList()
    }

private fun Board.attack(
    current: Cell.Piece.King,
    enemy: Cell.Piece,
    increase: Increase,
    enemyCount: Int
): List<AIMove> =
    attackAiMoveOrNull(current, enemy, increase)
        ?.let {
            val next = it.board.getAvailableCells(
                current = it.updatedCell as Cell.Piece.King,
                increase = increase,
                enemyCount = enemyCount + 1
            )
            listOf(it) + next
        }
        ?: emptyList()

private fun Board.simpleMove(
    current: Cell.Piece.King,
    cell: Cell.Empty,
    increase: Increase,
    enemyCount: Int
): List<AIMove> =
    aiMove(current = current, new = cell)
        .let {
            val next = it.board.getAvailableCells(
                current = it.updatedCell as Cell.Piece.King,
                increase = increase,
                enemyCount = enemyCount
            )
            listOf(it) + next
        }