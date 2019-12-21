package com.kubsu.checkers.functions.move.human

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.MoveResult
import com.kubsu.checkers.data.game.MoveType
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.data.game.updateFor
import com.kubsu.checkers.left
import com.kubsu.checkers.right
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.abs

internal fun Board.move(
    start: Cell.Piece.King,
    finish: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, MoveResult> =
    if (start onDiagonal finish) {
        val intermediateCells = getIntermediateCells(start, finish)
        if (isSimpleMove(intermediateCells)) {
            Either.right(simpleMove(start, finish, score))
        } else {
            val enemy = intermediateCells.geSingleEnemyOrNull(start)
            if (enemy != null && isAttack(start, enemy))
                Either.right(attack(start, finish, enemy, score))
            else
                Either.left(Failure.IncorrectMove)
        }
    } else {
        Either.left(Failure.IncorrectMove)
    }

private infix fun Cell.onDiagonal(cell: Cell): Boolean =
    difference(row, cell.row) == difference(column, cell.column)

@Suppress("NOTHING_TO_INLINE")
private inline fun difference(a: Int, b: Int): Int = abs(a - b)

private fun Board.getIntermediateCells(
    start: Cell,
    finish: Cell
): ImmutableList<Cell> {
    val (lower, higher) = getLowerAndHigher(start, finish)
    return getIntermediateCells(
        start = higher,
        finish = lower,
        columnIncrease = if (higher.column < lower.column) 1 else -1
    )
}

private data class PairCell(val lower: Cell, val higher: Cell)

private fun getLowerAndHigher(first: Cell, second: Cell): PairCell =
    if (first.row > second.row)
        PairCell(lower = first, higher = second)
    else
        PairCell(lower = second, higher = first)

private fun Board.getIntermediateCells(
    start: Cell,
    finish: Cell,
    columnIncrease: Int
): ImmutableList<Cell> {
    val result = mutableListOf<Cell>()
    var row = start.row + 1
    var column = start.column + columnIncrease
    while (row < finish.row) {
        result.add(requireNotNull(get(row, column)))
        row++; column += columnIncrease
    }
    return result.toImmutableList()
}

private fun isSimpleMove(intermediateCells: ImmutableList<Cell>): Boolean =
    intermediateCells.all { it is Cell.Empty }

private fun isAttack(start: Cell.Piece.King, enemy: Cell.Piece): Boolean =
    start isEnemy enemy

private fun Collection<Cell>.geSingleEnemyOrNull(start: Cell.Piece): Cell.Piece? =
    filterIsInstance<Cell.Piece>().singleOrNull(start::isEnemy)

private fun Board.attack(
    start: Cell.Piece.King,
    finish: Cell.Empty,
    enemy: Cell.Piece,
    score: Score
) =
    MoveResult(
        board = swap(start, finish).update(enemy.toEmpty()),
        score = score updateFor start,
        nextMove = start.color,
        moveType = MoveType.Attack
    )