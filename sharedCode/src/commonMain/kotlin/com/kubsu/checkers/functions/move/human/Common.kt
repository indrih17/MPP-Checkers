package com.kubsu.checkers.functions.move.human

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.*
import com.kubsu.checkers.map
import com.kubsu.checkers.right

fun UserGameState.makeMove(finishCell: Cell): Either<Failure.IncorrectMove, UserGameState> =
    if (startCell == null)
        justSelected(gameState, finishCell).right()
    else
        when (finishCell) {
            is Cell.Piece ->
                if (startCell isSameColor finishCell)
                    UserGameState(gameState, finishCell).right()
                else
                    this.right()

            is Cell.Empty ->
                gameState.updateGameState(startCell, finishCell, gameState.score)
        }

private fun justSelected(gameState: GameState, finish: Cell): UserGameState =
    if (finish is Cell.Piece && gameState.activePlayerColor == finish.color)
        UserGameState(
            gameState = gameState.copy(activePlayerColor = finish.color),
            startCell = finish
        )
    else
        UserGameState(gameState, null)

private fun GameState.updateGameState(
    start: Cell.Piece,
    finish: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, UserGameState> =
    board
        .move(start, finish, score, simpleMoves)
        .map {
            UserGameState(
                gameState = it.copy(
                    simpleMoves = if (it.score == score) simpleMoves + 1 else 0
                ),
                startCell = null
            )
        }

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
        activePlayerColor = start.color.enemy(),
        simpleMoves = simpleMoves + 1
    )