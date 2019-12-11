package com.kubsu.checkers.functions.move

import com.kubsu.checkers.data.*

fun cellWasSelected(gameState: GameState, cell: Cell): Either<Failure.IncorrectMove, GameState> {
    val selected = gameState.selectedCell
    return when {
        selected == null ->
            Either.right(justSelected(gameState, cell))

        cell is Cell.Piece && cell isSameColor selected ->
            Either.right(gameState.copy(selectedCell = cell))

        cell is Cell.Empty ->
            gameState.board.updateGameState(selected, cell, gameState.score)

        else ->
            Either.right(gameState)
    }
}

private fun justSelected(gameState: GameState, cell: Cell): GameState =
    if (cell is Cell.Piece && cell.color == gameState.activePlayerColor)
        gameState.copy(
            activePlayerColor = cell.color,
            selectedCell = cell
        )
    else
        gameState

private fun Board.updateGameState(
    current: Cell.Piece,
    destination: Cell.Empty,
    score: Score
): Either<Failure.IncorrectMove, GameState> =
    move(current, destination, score).map {
        GameState(
            board = it.board,
            score = it.score,
            activePlayerColor = it.nextMove,
            selectedCell = null
        )
    }

private fun Board.move(
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
        nextMove = current.colorOfEnemy()
    )