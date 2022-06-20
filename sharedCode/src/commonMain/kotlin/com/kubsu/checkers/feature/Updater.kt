package com.kubsu.checkers.feature

import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.functions.createGameState
import family.amma.keemun.Update

sealed class CheckersMsg

sealed class ExternalMsg : CheckersMsg() {
    data class CellSelected(val cell: Cell) : ExternalMsg()
    data class ChangeGameType(val gameType: GameType) : ExternalMsg()
}

sealed class InternalMsg : CheckersMsg() {
    data class ShowFailure(val failure: Failure) : InternalMsg()
    data class ApplyGameState(val gameState: GameState, val gameType: GameType) : InternalMsg()
}

sealed class CheckersEffect {
    data class UpdateGameType(val gameType: GameType) : CheckersEffect()

    data class PerformUserAction(
        val gameState: GameState.Continues,
        val gameType: GameType,
        val startPiece: Cell.Piece,
        val finishCell: Cell.Empty
    ) : CheckersEffect()
    
    data class CalculateNextAiMove(val gameState: GameState.Continues, val gameType: GameType) : CheckersEffect()
}

fun externalUpdater() = Update<CheckersState, ExternalMsg, CheckersEffect> { msg, state ->
    when (msg) {
        is ExternalMsg.ChangeGameType -> {
            if (state.gameType != msg.gameType) {
                val defaultGameState = createGameState(playerColor = CellColor.Light)
                state.copy(
                    gameType = msg.gameType,
                    gameState = defaultGameState,
                    startPiece = null,
                    failure = null
                ) to setOfNotNull(
                    CheckersEffect.UpdateGameType(msg.gameType),
                    calculateNextAiMoveOrNull(defaultGameState, msg.gameType)
                )
            } else {
                state to emptySet()
            }
        }

        is ExternalMsg.CellSelected ->
            if (state.gameState is GameState.Continues) {
                when (msg.cell) {
                    is Cell.Piece ->
                        state.copy(
                            startPiece = msg.cell
                                .takeIf { it.color == state.gameState.activePlayer }
                                ?: state.startPiece,
                            failure = null
                        ) to emptySet()

                    is Cell.Empty ->
                        state.copy(failure = null) to setOfNotNull(
                            if (state.startPiece != null) {
                                CheckersEffect.PerformUserAction(
                                    state.gameState, state.gameType, state.startPiece, msg.cell
                                )
                            } else {
                                null
                            }
                        )
                }
            } else {
                state to emptySet()
            }
    }
}

fun internalUpdater() = Update<CheckersState, InternalMsg, CheckersEffect> { msg, state ->
    if (state.gameState is GameState.Continues) {
        when (msg) {
            is InternalMsg.ShowFailure ->
                state.copy(failure = msg.failure) to emptySet()

            is InternalMsg.ApplyGameState ->
                if (state.gameType == msg.gameType) {
                    state.copy(gameState = msg.gameState, startPiece = null, failure = null) to setOfNotNull(
                        calculateNextAiMoveOrNull(msg.gameState, state.gameType)
                    )
                } else {
                    state to emptySet()
                }
        }
    } else {
        state to emptySet()
    }
}
