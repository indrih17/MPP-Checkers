package com.kubsu.checkers.functions.man

import com.kubsu.checkers.data.*
import kotlin.math.abs

fun Board.move(
    current: Cell.Piece.Man,
    destination: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, MoveResult> =
    when {
        current isSimpleMove destination ->
            Either.right(MoveResult(simpleMove(current, destination), score))

        current isAttack destination ->
            attack(current, destination, score)

        else ->
            Either.left(Failure.IncorrectMove)
    }.map { moveResult ->
        with(moveResult.board) {
            if (needToMadeKing(current))
                moveResult.copy(board = setKing(current))
            else
                moveResult
        }
    }

private infix fun Cell.Piece.Man.isSimpleMove(destination: Cell.Empty): Boolean =
    this toLeftOf destination || this toRightOf destination

private infix fun Cell.Piece.Man.toLeftOf(destination: Cell.Empty): Boolean =
    when (color) {
        Color.White -> row - 1 == destination.row && column - 1 == destination.column
        Color.Black -> row + 1 == destination.row && column + 1 == destination.column
    }

private infix fun Cell.Piece.Man.toRightOf(destination: Cell.Empty): Boolean =
    when (color) {
        Color.White -> row - 1 == destination.row && column + 1 == destination.column
        Color.Black -> row + 1 == destination.row && column - 1 == destination.column
    }

private infix fun Cell.Piece.Man.isAttack(destination: Cell.Empty): Boolean =
    isInAttackZone(row, destination.row) && isInAttackZone(column, destination.column)

@Suppress("NOTHING_TO_INLINE")
private inline fun isInAttackZone(a: Int, b: Int): Boolean = abs(a - b) == 2

private fun Board.simpleMove(
    current: Cell.Piece.Man,
    destination: Cell.Empty
): Board =
    swap(current, destination)

private fun Board.attack(
    current: Cell.Piece.Man,
    destination: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, MoveResult> {
    val middleCell = middle(current, destination)
    return if (middleCell is Cell.Piece && current isEnemy middleCell)
        Either.right(
            MoveResult(
                board = swap(current, destination).update(middleCell.toEmpty()),
                score = score updateFor current.color
            )
        )
    else
        Either.left(Failure.IncorrectMove)
}

private fun Board.middle(first: Cell.Piece, second: Cell.Empty): Cell =
    get(
        row = average(first.row, second.row),
        column = average(first.column, second.column)
    )

@Suppress("NOTHING_TO_INLINE")
private inline fun average(a: Int, b: Int): Int = (a + b) / 2

private fun Board.needToMadeKing(cell: Cell.Piece.Man): Boolean =
    cell.row == if (cell.color is Color.White) firstIndex else lastIndex

private fun Board.setKing(cell: Cell.Piece.Man): Board =
    update(cell.toKing())