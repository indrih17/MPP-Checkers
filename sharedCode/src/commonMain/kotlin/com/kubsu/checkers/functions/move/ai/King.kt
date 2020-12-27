package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.entities.increasesSequence

internal fun Board.getAvailableCellsSequence(current: Cell.Piece.King): Sequence<Board> =
    increasesSequence
        .map { increase -> getAvailableMoves(current, increase) }
        .flatten()

internal fun Board.attack(
    current: Cell.Piece.King,
    enemy: Cell.Piece,
    increase: Increase,
    enemyCount: Int
): List<Board> =
    attackAiMoveOrNull(current, enemy, increase)
        ?.let { (board, updatedCell) ->
            val next = board.getAvailableMoves(
                current = updatedCell as Cell.Piece.King,
                increase = increase,
                enemyCount = enemyCount + 1
            )
            listOf(board) + next
        }
        ?: emptyList()

internal fun Board.simpleMove(
    current: Cell.Piece.King,
    cell: Cell.Empty,
    increase: Increase,
    enemyCount: Int
): List<Board> =
    aiMove(current = current, new = cell)
        .let { (board, updatedCell) ->
            val next = board.getAvailableMoves(
                current = updatedCell as Cell.Piece.King,
                increase = increase,
                enemyCount = enemyCount
            )
            listOf(board) + next
        }

private fun Board.getAvailableMoves(
    current: Cell.Piece.King,
    increase: Increase,
    enemyCount: Int = 0
): List<Board> =
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
