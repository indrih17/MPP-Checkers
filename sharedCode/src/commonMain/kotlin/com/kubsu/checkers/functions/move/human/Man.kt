package com.kubsu.checkers.functions.move.human

import com.kubsu.checkers.*
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.functions.move.kill

internal fun Board.move(
    man: Cell.Piece.Man,
    destination: Cell.Empty,
    simpleMoves: Int
): Either<Failure.IncorrectMove, GameState> =
    if (isSimpleMove(man, destination)) {
        simpleMove(man, destination, simpleMoves).right()
    } else {
        val middleCell = middle(man, destination)
        if (middleCell is Cell.Piece && isAttack(man, destination, middleCell))
            attack(man, destination, middleCell).right()
        else
            Failure.IncorrectMove(man, destination).left()
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

/** @return true if [man] can attack [middle] and go to [empty]. */
internal fun isAttack(
    man: Cell.Piece.Man,
    empty: Cell.Empty,
    middle: Cell.Piece
): Boolean =
    man isEnemy middle && isInAttackZone(man, empty)

/** @return true if [man] can move into [cell]. */
@Suppress("NOTHING_TO_INLINE")
private inline fun isInAttackZone(man: Cell.Piece.Man, cell: Cell.Empty): Boolean =
    difference(man.row, cell.row) == 2 && difference(man.column, cell.column) == 2

/** Update [GameState]: [enemy] killed, [man] goes to [destination]. */
internal fun Board.attack(
    man: Cell.Piece.Man,
    destination: Cell.Empty,
    enemy: Cell.Piece,
) =
    GameState(
        board = kill(man, enemy, destination).first,
        activePlayer = man.color.enemy,
        simpleMoves = 0
    )

/** Average number between [a] and [b]. */
@Suppress("NOTHING_TO_INLINE")
private inline fun average(a: Int, b: Int): Int = (a + b) / 2
