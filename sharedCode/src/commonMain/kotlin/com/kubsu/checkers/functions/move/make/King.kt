package com.kubsu.checkers.functions.move.make

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.MoveResult
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.data.game.updateFor
import com.kubsu.checkers.left
import com.kubsu.checkers.right
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.abs

internal fun Board.move(
    current: Cell.Piece.King,
    destination: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, MoveResult> =
    if (current onDiagonal destination) {
        val intermediateCells = getIntermediateCells(current, destination)
        if (isSimpleMove(intermediateCells)) {
            Either.right(simpleMove(current, destination, score))
        } else {
            val enemy = intermediateCells.geSingleEnemyOrNull(current)
            if (enemy != null && isAttack(current, enemy))
                Either.right(attack(current, destination, enemy, score))
            else
                Either.left(Failure.IncorrectMove)
        }
    } else {
        Either.left(Failure.IncorrectMove)
    }

private infix fun Cell.onDiagonal(cell: Cell): Boolean =
    difference(
        row,
        cell.row
    ) == difference(column, cell.column)

@Suppress("NOTHING_TO_INLINE")
private inline fun difference(a: Int, b: Int): Int = abs(a - b)

private fun Board.getIntermediateCells(
    current: Cell,
    destination: Cell
): ImmutableList<Cell> {
    val (lower, higher) = getLowerAndHigher(
        current,
        destination
    )
    return getIntermediateCells(
        current = higher,
        destination = lower,
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
    current: Cell,
    destination: Cell,
    columnIncrease: Int
): ImmutableList<Cell> {
    val result = mutableListOf<Cell>()
    var row = current.row + 1
    var column = current.column + columnIncrease
    while (row < destination.row) {
        result.add(requireNotNull(get(row, column)))
        row++; column += columnIncrease
    }
    return result.toImmutableList()
}

private fun isSimpleMove(intermediateCells: ImmutableList<Cell>): Boolean =
    intermediateCells.all { it is Cell.Empty }

private fun isAttack(current: Cell.Piece.King, enemy: Cell.Piece): Boolean =
    current isEnemy enemy

private fun Collection<Cell>.geSingleEnemyOrNull(current: Cell.Piece): Cell.Piece? =
    filterIsInstance<Cell.Piece>().singleOrNull(current::isEnemy)

private fun Board.attack(
    current: Cell.Piece.King,
    destination: Cell.Empty,
    enemy: Cell.Piece,
    score: Score
) =
    MoveResult(
        board = swap(current, destination).update(enemy.toEmpty()),
        score = score updateFor current,
        nextMove = current.color
    )