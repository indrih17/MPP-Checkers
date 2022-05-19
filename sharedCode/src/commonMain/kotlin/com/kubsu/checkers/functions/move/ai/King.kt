package com.kubsu.checkers.functions.move.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.entities.increasesSequence
import com.kubsu.checkers.functions.move.move

internal fun Board.getAvailableCellsSequence(current: Cell.Piece.King): Sequence<Board> =
    increasesSequence
        .map { increase -> getAvailableMoves(current, increase) }
        .flatten()

private fun Board.getAvailableMoves(
    king: Cell.Piece.King,
    increase: Increase,
    enemyCount: Int = 0
): List<Board> =
    when (val cell = get(king, increase)) {
        is Cell.Piece ->
            if (enemyCount == 0 && cell isEnemy king)
                attack(king, cell, increase, enemyCount)
            else
                emptyList()

        is Cell.Empty ->
            simpleMove(king, cell, increase, enemyCount)

        null -> emptyList()
    }

private fun Board.attack(
    king: Cell.Piece.King,
    enemy: Cell.Piece,
    increase: Increase,
    enemyCount: Int
): List<Board> =
    attackAiMoveOrNull(king, enemy, increase)
        ?.let { (board, updatedCell) ->
            val next: List<Board> = board.getAvailableMoves(
                king = updatedCell,
                increase = increase,
                enemyCount = enemyCount + 1
            )
            listOf(board) + next
        }
        ?: emptyList()

private fun Board.simpleMove(
    king: Cell.Piece.King,
    destination: Cell.Empty,
    increase: Increase,
    enemyCount: Int
): List<Board> =
    move(current = king, empty = destination)
        .let { (board, updatedCell) ->
            val next: List<Board> = board.getAvailableMoves(
                king = updatedCell,
                increase = increase,
                enemyCount = enemyCount
            )
            listOf(board) + next
        }