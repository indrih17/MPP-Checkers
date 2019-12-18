package com.kubsu.checkers.functions.move.make

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.enemy
import com.kubsu.checkers.data.entities.swap
import com.kubsu.checkers.data.game.MoveResult
import com.kubsu.checkers.data.game.MoveType
import com.kubsu.checkers.data.game.Score

internal fun Board.move(
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
    destination: Cell.Empty,
    score: Score
) =
    MoveResult(
        board = swap(current, destination),
        score = score,
        nextMove = current.color.enemy(),
        moveType = MoveType.SimpleMove
    )