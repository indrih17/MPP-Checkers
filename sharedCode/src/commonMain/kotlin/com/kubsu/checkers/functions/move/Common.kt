package com.kubsu.checkers.functions.move

import com.kubsu.checkers.data.*

fun Board.move(
    current: Cell.Piece,
    destination: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, MoveResult> =
    when (current) {
        is Cell.Piece.Man -> move(current, destination, score)
        is Cell.Piece.King -> move(current, destination, score)
    }

internal fun Board.simpleMove(
    current: Cell.Piece,
    destination: Cell.Empty
): Board =
    swap(current, destination)