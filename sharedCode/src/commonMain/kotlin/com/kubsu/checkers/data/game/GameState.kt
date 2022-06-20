package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.functions.GameResult
import com.kubsu.checkers.functions.gameResultOrNull
import kotlinx.serialization.Serializable

@Serializable
sealed class GameState {
    abstract val board: @Serializable(with = BoardSerializer::class) Board

    @Serializable
    data class GameOver(
        val gameResult: GameResult,
        override val board: @Serializable(with = BoardSerializer::class) Board,
    ) : GameState()

    /**
     * @param activePlayer The player currently taking the move.
     * @param simpleMoves The total number of moves without kills.
     */
    @Serializable
    data class Continues(
        override val board: @Serializable(with = BoardSerializer::class) Board,
        val simpleMoves: Int,
        val activePlayer: CellColor
    ) : GameState()
}

internal fun GameState.Continues.apply(board: Board): GameState =
    gameResultOrNull()
        ?.let { GameState.GameOver(gameResult = it, board = board) }
        ?: kotlin.run {
            val newScore = board.getScore()
            copy(
                board = board,
                activePlayer = activePlayer.enemy,
                simpleMoves = if (newScore == this.board.getScore()) simpleMoves + 1 else 0
            )
        }

/** Game score. */
data class Score(
    val light: Int,
    val dark: Int
)

/** @return the calculated game score. */
fun Board.getScore(): Score {
    val pieces = filterIsInstance<Cell.Piece>()
    return Score(
        light = piecesAmount - pieces.count { it.color == CellColor.Dark },
        dark = piecesAmount - pieces.count { it.color == CellColor.Light }
    )
}
