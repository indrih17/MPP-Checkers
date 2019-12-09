package com.kubsu.checkers

import com.kubsu.checkers.data.*
import kotlin.math.abs

data class AttackResult(val board: Board, val score: Score)

fun Board.attack(
    current: Cell.Piece.Man,
    destination: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, AttackResult> =
    middle(current, destination).flatMap { middleCell ->
        if (middleCell is Cell.Piece && current isEnemy middleCell)
            Either.right(
                AttackResult(
                    board = swap(current, destination).update(middleCell.toEmpty()),
                    score = score updateFor current.color
                )
            )
        else
            Either.left(Failure.IncorrectMove)
    }

private fun Board.middle(first: Cell.Piece, second: Cell.Empty): Either<Failure.IncorrectMove, Cell> =
    if (abs(first.row - second.row) == 2 && abs(first.column - second.column) == 2)
        Either.right(get((first.row + second.row) / 2, (first.column + second.column) / 2))
    else
        Either.left(Failure.IncorrectMove)