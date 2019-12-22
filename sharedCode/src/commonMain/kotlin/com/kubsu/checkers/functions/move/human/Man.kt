package com.kubsu.checkers.functions.move.human

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.MoveResult
import com.kubsu.checkers.data.game.MoveType
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.data.game.updateFor
import com.kubsu.checkers.left
import com.kubsu.checkers.map
import com.kubsu.checkers.right
import kotlin.math.abs

internal fun Board.move(
    start: Cell.Piece.Man,
    finish: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, MoveResult> {
    val either = if (isSimpleMove(start, finish)) {
        Either.right(simpleMove(start, finish, score))
    } else {
        val middleCell = middle(start, finish)
        if (middleCell is Cell.Piece && isAttack(start, finish, middleCell))
            Either.right(attack(start, finish, middleCell, score))
        else
            Either.left(Failure.IncorrectMove(start, finish))
    }
    return either.map { it.checkAndSetKing(start, finish) }
}

private fun MoveResult.checkAndSetKing(
    start: Cell.Piece.Man,
    finish: Cell.Empty
): MoveResult {
    val new = start.updateCoordinates(finish) as Cell.Piece.Man
    return if (board.needToMadeKing(new))
        copy(board = board.setKing(new))
    else
        this
}

private fun isSimpleMove(start: Cell.Piece.Man, finish: Cell.Empty): Boolean =
    start toLeftOf finish || start toRightOf finish

private infix fun Cell.Piece.Man.toLeftOf(finish: Cell.Empty): Boolean =
    when (color) {
        CellColor.Light -> row - 1 == finish.row && column + 1 == finish.column
        CellColor.Dark -> row + 1 == finish.row && column - 1 == finish.column
    }

private infix fun Cell.Piece.Man.toRightOf(finish: Cell.Empty): Boolean =
    when (color) {
        CellColor.Light -> row - 1 == finish.row && column - 1 == finish.column
        CellColor.Dark -> row + 1 == finish.row && column + 1 == finish.column
    }

private fun isAttack(
    start: Cell.Piece.Man,
    finish: Cell.Empty,
    middle: Cell.Piece
): Boolean =
    isInAttackZone(start.row, finish.row)
            && isInAttackZone(start.column, finish.column)
            && start isEnemy middle

@Suppress("NOTHING_TO_INLINE")
private inline fun isInAttackZone(a: Int, b: Int): Boolean = abs(a - b) == 2

private fun Board.middle(first: Cell.Piece, second: Cell.Empty): Cell =
    requireNotNull(
        get(
            row = average(first.row, second.row),
            column = average(first.column, second.column)
        )
    )

private fun Board.attack(
    start: Cell.Piece.Man,
    finish: Cell.Empty,
    middle: Cell.Piece,
    score: Score
) =
    MoveResult(
        board = swap(start, finish).update(middle.toEmpty()),
        score = score updateFor start,
        nextMove = start.color.enemy(),
        moveType = MoveType.Attack
    )

@Suppress("NOTHING_TO_INLINE")
private inline fun average(a: Int, b: Int): Int = (a + b) / 2

internal fun Board.needToMadeKing(cell: Cell.Piece.Man): Boolean =
    cell.row == if (cell.color is CellColor.Light) firstIndex else lastIndex

private fun Board.setKing(cell: Cell.Piece.Man): Board =
    update(cell.toKing())