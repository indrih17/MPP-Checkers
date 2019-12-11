package com.kubsu.checkers.functions.move

import com.kubsu.checkers.data.*
import kotlin.math.abs

internal fun Board.move(
    current: Cell.Piece.Man,
    destination: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, MoveResult> {
    val either = if (isSimpleMove(current, destination)) {
        Either.right(simpleMove(current, destination, score))
    } else {
        val middleCell = middle(current, destination)
        if (middleCell is Cell.Piece && isAttack(current, destination, middleCell))
            Either.right(attack(current, destination, middleCell, score))
        else
            Either.left(Failure.IncorrectMove)
    }
    return either.map { it.checkAndSetKing(current, destination) }
}

private fun MoveResult.checkAndSetKing(
    current: Cell.Piece.Man,
    destination: Cell.Empty
): MoveResult {
    val new = current.updateCoordinates(destination) as Cell.Piece.Man
    return if (board.needToMadeKing(new))
        copy(board = board.setKing(new))
    else
        this
}

private fun isSimpleMove(current: Cell.Piece.Man, destination: Cell.Empty): Boolean =
    current toLeftOf destination || current toRightOf destination

private infix fun Cell.Piece.Man.toLeftOf(destination: Cell.Empty): Boolean =
    when (color) {
        CellColor.White -> row - 1 == destination.row && column + 1 == destination.column
        CellColor.Black -> row + 1 == destination.row && column - 1 == destination.column
    }

private infix fun Cell.Piece.Man.toRightOf(destination: Cell.Empty): Boolean =
    when (color) {
        CellColor.White -> row - 1 == destination.row && column - 1 == destination.column
        CellColor.Black -> row + 1 == destination.row && column + 1 == destination.column
    }

private fun isAttack(
    current: Cell.Piece.Man,
    destination: Cell.Empty,
    middleCell: Cell.Piece
): Boolean =
    isInAttackZone(current.row, destination.row)
            && isInAttackZone(current.column, destination.column)
            && current isEnemy middleCell

@Suppress("NOTHING_TO_INLINE")
private inline fun isInAttackZone(a: Int, b: Int): Boolean = abs(a - b) == 2

private fun Board.middle(first: Cell.Piece, second: Cell.Empty): Cell =
    get(
        row = average(first.row, second.row),
        column = average(first.column, second.column)
    )

private fun Board.attack(
    current: Cell.Piece.Man,
    destination: Cell.Empty,
    middleCell: Cell.Piece,
    score: Score
) =
    MoveResult(
        board = swap(current, destination).update(middleCell.toEmpty()),
        score = score updateFor current,
        nextMove = current.color
    )

@Suppress("NOTHING_TO_INLINE")
private inline fun average(a: Int, b: Int): Int = (a + b) / 2

fun Board.needToMadeKing(cell: Cell.Piece.Man): Boolean =
    cell.row == if (cell.color is CellColor.White) firstIndex else lastIndex

private fun Board.setKing(cell: Cell.Piece.Man): Board =
    update(cell.toKing())