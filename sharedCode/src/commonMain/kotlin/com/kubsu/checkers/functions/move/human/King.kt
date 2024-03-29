package com.kubsu.checkers.functions.move.human

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.apply
import com.kubsu.checkers.difference
import com.kubsu.checkers.functions.move.kill
import com.kubsu.checkers.functions.move.move
import com.kubsu.checkers.left
import com.kubsu.checkers.right
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun GameState.Continues.move(
    king: Cell.Piece.King,
    destination: Cell.Empty,
): Either<Failure.IncorrectMove, GameState> =
    if (king onDiagonal destination) {
        val intermediateCells = board.getIntermediateCells(king, destination)
        if (isSimpleMove(intermediateCells)) {
            apply(board = board.move(king, destination).first).right()
        } else {
            val enemy = intermediateCells.geSingleEnemyOrNull(king)
            if (enemy != null && isAttack(king, enemy))
                apply(board = board.kill(king, enemy, destination).first).right()
            else
                Failure.IncorrectMove(king, destination).left()
        }
    } else {
        Failure.IncorrectMove(king, destination).left()
    }

private infix fun Cell.onDiagonal(cell: Cell): Boolean =
    difference(row, cell.row) == difference(column, cell.column)

private fun Board.getIntermediateCells(
    king: Cell.Piece.King,
    destination: Cell.Empty
): ImmutableList<Cell> {
    val (bottom, top) = getBottomAndTop(king, destination)
    return getIntermediateCells(
        top = top,
        bottom = bottom,
        columnIncrease = if (top.column < bottom.column) 1 else -1
    )
}

/** @return pair of bottom (row) cell and top (row too) cell. */
private fun getBottomAndTop(first: Cell, second: Cell): Pair<Cell, Cell> =
    if (first.row > second.row)
        first to second
    else
        second to first

private fun Board.getIntermediateCells(
    top: Cell,
    bottom: Cell,
    columnIncrease: Int
): ImmutableList<Cell> {
    val result = mutableListOf<Cell>()
    var row = top.row + 1
    var column = top.column + columnIncrease
    while (row < bottom.row) {
        result.add(requireNotNull(get(row, column)))
        row++; column += columnIncrease
    }
    return result.toImmutableList()
}

private fun isSimpleMove(intermediateCells: ImmutableList<Cell>): Boolean =
    intermediateCells.all { it is Cell.Empty }

private fun isAttack(king: Cell.Piece.King, piece: Cell.Piece): Boolean =
    king isEnemy piece

private fun Iterable<Cell>.geSingleEnemyOrNull(piece: Cell.Piece): Cell.Piece? =
    filterIsInstance<Cell.Piece>().singleOrNull(piece::isEnemy)
