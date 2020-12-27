package com.kubsu.checkers.functions.move.human

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.*
import com.kubsu.checkers.background
import com.kubsu.checkers.map
import com.kubsu.checkers.right

suspend fun UserState.makeMove(finishCell: Cell): Either<Failure.IncorrectMove, UserState> =
    background { move(finishCell) }

internal fun UserState.move(finishCell: Cell): Either<Failure.IncorrectMove, UserState> =
    if (startPiece == null) {
        justSelected(gameState, finishCell).right()
    } else {
        when (finishCell) {
            is Cell.Piece ->
                if (startPiece isSameColor finishCell)
                    UserState(gameState, finishCell).right()
                else
                    this.right()

            is Cell.Empty ->
                gameState.updateGameState(startPiece, finishCell, gameState.score)
        }
    }

private fun justSelected(gameState: GameState, finish: Cell): UserState =
    if (finish is Cell.Piece && gameState.activePlayer == finish.color)
        UserState(gameState, startPiece = finish)
    else
        UserState(gameState, startPiece = null)

private fun GameState.updateGameState(
    start: Cell.Piece,
    finish: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, UserState> =
    board
        .move(start, finish, score, simpleMoves)
        .map { UserState(gameState = it, startPiece = null) }

private fun Board.move(
    start: Cell.Piece,
    finish: Cell.Empty,
    score: Score,
    simpleMoves: Int
): Either<Failure.IncorrectMove, GameState> =
    when (start) {
        is Cell.Piece.Man -> move(start, finish, score, simpleMoves)
        is Cell.Piece.King -> move(start, finish, score, simpleMoves)
    }

internal fun Board.simpleMove(
    start: Cell.Piece,
    finish: Cell.Empty,
    score: Score,
    simpleMoves: Int
) =
    GameState(
        board = swap(start, finish),
        score = score,
        simpleMoves = simpleMoves + 1,
        activePlayer = start.color.enemy()
    )