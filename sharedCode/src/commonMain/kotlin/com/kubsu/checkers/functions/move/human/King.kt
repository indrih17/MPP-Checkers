package com.kubsu.checkers.functions.move.human

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.ActivePlayer
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.data.game.updateFor
import com.kubsu.checkers.difference
import com.kubsu.checkers.left
import com.kubsu.checkers.right
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal fun Board.move(
    start: Cell.Piece.King,
    finish: Cell.Empty,
    score: Score,
    simpleMoves: Int
): Either<Failure.IncorrectMove, GameState> =
    if (start onDiagonal finish) {
        val intermediateCells = getIntermediateCells(start, finish)
        if (isSimpleMove(intermediateCells)) {
            simpleMove(start, finish, score, simpleMoves).right()
        } else {
            val enemy = intermediateCells.geSingleEnemyOrNull(start)
            if (enemy != null && isAttack(start, enemy))
                attack(start, finish, enemy, score).right()
            else
                Failure.IncorrectMove(start, finish).left()
        }
    } else {
        Failure.IncorrectMove(start, finish).left()
    }

private infix fun Cell.onDiagonal(cell: Cell): Boolean =
    difference(row, cell.row) == difference(column, cell.column)

internal fun Board.getIntermediateCells(
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

internal data class PairCell(val lower: Cell, val higher: Cell)

internal fun getLowerAndHigher(first: Cell, second: Cell): PairCell =
    if (first.row > second.row)
        PairCell(lower = first, higher = second)
    else
        PairCell(lower = second, higher = first)

internal fun Board.getIntermediateCells(
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

internal fun isSimpleMove(intermediateCells: ImmutableList<Cell>): Boolean =
    intermediateCells.all { it is Cell.Empty }

internal fun isAttack(start: Cell.Piece.King, enemy: Cell.Piece): Boolean =
    start isEnemy enemy

internal fun Collection<Cell>.geSingleEnemyOrNull(start: Cell.Piece): Cell.Piece? =
    filterIsInstance<Cell.Piece>().singleOrNull(start::isEnemy)

internal fun Board.attack(
    start: Cell.Piece.King,
    finish: Cell.Empty,
    enemy: Cell.Piece,
    score: Score
) =
    GameState(
        board = swap(start, finish).update(enemy.toEmpty()),
        score = score updateFor start,
        activePlayer = ActivePlayer(
            color = start.color.enemy(),
            simpleMoves = 0
        )
    )