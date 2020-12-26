package com.kubsu.checkers.functions.move.human

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.ActivePlayer
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.data.game.updateFor
import com.kubsu.checkers.left
import com.kubsu.checkers.map
import com.kubsu.checkers.right
import kotlin.math.abs

internal fun Board.move(
    start: Cell.Piece.Man,
    finish: Cell.Empty,
    score: Score,
    simpleMoves: Int
): Either<Failure.IncorrectMove, GameState> {
    val either = if (isSimpleMove(start, finish)) {
        simpleMove(start, finish, score, simpleMoves).right()
    } else {
        val middleCell = middle(start, finish)
        if (middleCell is Cell.Piece && isAttack(start, finish, middleCell))
            attack(start, finish, middleCell, score).right()
        else
            Failure.IncorrectMove(start, finish).left()
    }
    return either.map { it.checkAndSetKing(start, finish) }
}

/** @return true if move between [start] and [finish] is simple. */
internal fun isSimpleMove(start: Cell.Piece.Man, finish: Cell.Empty): Boolean =
    start toLeftOf finish || start toRightOf finish

private infix fun Cell.Piece.toLeftOf(finish: Cell): Boolean =
    when (color) {
        CellColor.Light -> row - 1 == finish.row && column + 1 == finish.column
        CellColor.Dark -> row + 1 == finish.row && column - 1 == finish.column
    }

private infix fun Cell.Piece.toRightOf(finish: Cell): Boolean =
    when (color) {
        CellColor.Light -> row - 1 == finish.row && column - 1 == finish.column
        CellColor.Dark -> row + 1 == finish.row && column + 1 == finish.column
    }

/** @return the middle cell between the [first] and [second].  */
internal fun Board.middle(first: Cell.Piece, second: Cell.Empty): Cell? =
    get(
        row = average(first.row, second.row),
        column = average(first.column, second.column)
    )

/** @return true if [start] can attack [middle] and go to [finish]. */
internal fun isAttack(
    start: Cell.Piece.Man,
    finish: Cell.Empty,
    middle: Cell.Piece
): Boolean =
    isInAttackZone(start.row, finish.row)
            && isInAttackZone(start.column, finish.column)
            && start isEnemy middle

@Suppress("NOTHING_TO_INLINE")
private inline fun isInAttackZone(a: Int, b: Int): Boolean = abs(a - b) == 2

/** Update [GameState]: [middle] killed, [start] goes to [finish], score was increased. */
internal fun Board.attack(
    start: Cell.Piece.Man,
    finish: Cell.Empty,
    middle: Cell.Piece,
    score: Score
) =
    GameState(
        board = swap(start, finish).update(middle.toEmpty()),
        score = score updateFor start,
        activePlayer = ActivePlayer(
            color = start.color.enemy(),
            simpleMoves = 0
        )
    )

@Suppress("NOTHING_TO_INLINE")
private inline fun average(a: Int, b: Int): Int = (a + b) / 2

internal fun GameState.checkAndSetKing(
    start: Cell.Piece.Man,
    finish: Cell.Empty
): GameState {
    val new = start.updateCoordinates(finish)
    return if (board.needToMadeKing(new))
        copy(board = board.setKing(new))
    else
        this
}

fun Board.needToMadeKing(cell: Cell.Piece.Man): Boolean =
    cell.row == if (cell.color is CellColor.Light) firstIndex else lastIndex

internal fun Board.setKing(cell: Cell.Piece.Man): Board =
    update(king(cell))
