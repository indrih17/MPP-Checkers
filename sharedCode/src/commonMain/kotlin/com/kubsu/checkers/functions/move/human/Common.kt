package com.kubsu.checkers.functions.move.human

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.*
import com.kubsu.checkers.map
import com.kubsu.checkers.right

fun MoveState.makeMove(finishCell: Cell): Either<Failure.IncorrectMove, MoveState> =
    if (startCell == null)
        justSelected(gameState, finishCell).right()
    else
        when (finishCell) {
            is Cell.Piece ->
                if (startCell isSameColor finishCell)
                    MoveState(gameState, finishCell).right()
                else
                    this.right()

            is Cell.Empty ->
                gameState.updateGameState(startCell, finishCell, gameState.score)
        }

private fun justSelected(gameState: GameState, finish: Cell): MoveState =
    if (finish is Cell.Piece && gameState.activePlayerColor == finish.color)
        MoveState(
            gameState = gameState.copy(activePlayerColor = finish.color),
            startCell = finish
        )
    else
        MoveState(gameState, null)

private fun GameState.updateGameState(
    start: Cell.Piece,
    finish: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, MoveState> =
    board.move(start, finish, score).map {
        MoveState(
            GameState(
                board = it.board,
                score = it.score,
                activePlayerColor = it.nextMove,
                movesWithoutAttacks = if (it.moveType == MoveType.SimpleMove)
                    movesWithoutAttacks + 1
                else
                    0
            ),
            startCell = null
        )
    }

private fun Board.move(
    start: Cell.Piece,
    finish: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, MoveResult> =
    when (start) {
        is Cell.Piece.Man -> move(start, finish, score)
        is Cell.Piece.King -> move(start, finish, score)
    }

internal fun Board.simpleMove(
    start: Cell.Piece,
    finish: Cell.Empty,
    score: Score
) =
    MoveResult(
        board = swap(start, finish),
        score = score,
        nextMove = start.color.enemy(),
        moveType = MoveType.SimpleMove
    )