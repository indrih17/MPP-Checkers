package com.kubsu.checkers.functions.move.human

import com.kubsu.checkers.Either
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.*
import com.kubsu.checkers.background
import com.kubsu.checkers.functions.updateSimpleMoves
import com.kubsu.checkers.map
import com.kubsu.checkers.right

suspend fun UserState.move(finishCell: Cell): Either<Failure.IncorrectMove, UserState> =
    background { makeMove(finishCell) }

internal fun UserState.makeMove(finishCell: Cell): Either<Failure.IncorrectMove, UserState> =
    if (startCell == null) {
        justSelected(gameState, finishCell).right()
    } else {
        when (finishCell) {
            is Cell.Piece ->
                if (startCell isSameColor finishCell)
                    UserState(gameState, finishCell).right()
                else
                    this.right()

            is Cell.Empty ->
                gameState.updateGameState(startCell, finishCell, gameState.score)
        }
    }

internal fun justSelected(gameState: GameState, finish: Cell): UserState =
    if (finish is Cell.Piece && gameState.activePlayer.color == finish.color)
        UserState(gameState, startCell = finish)
    else
        UserState(gameState, startCell = null)

internal fun GameState.updateGameState(
    start: Cell.Piece,
    finish: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, UserState> =
    board
        .move(start, finish, score, activePlayer.simpleMoves)
        .map {
            UserState(
                gameState = it.updateSimpleMoves(score),
                startCell = null
            )
        }

internal fun Board.move(
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
        activePlayer = ActivePlayer(
            color = start.color.enemy(),
            simpleMoves = simpleMoves + 1
        )
    )