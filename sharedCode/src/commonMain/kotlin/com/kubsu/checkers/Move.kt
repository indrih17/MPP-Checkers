package com.kubsu.checkers

import com.kubsu.checkers.data.*

fun Board.move(
    current: Cell.Piece.Man,
    destination: Cell.Empty
): Either<Failure.IncorrectMove, Board> =
    if (current toLeftOf destination || current toRightOf destination)
        Either.right(swap(current, destination))
    else
        Either.left(Failure.IncorrectMove)

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
