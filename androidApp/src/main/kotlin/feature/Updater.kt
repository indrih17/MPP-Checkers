package feature

import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.game.GameState
import family.amma.keemun.Update

sealed class CheckersMsg

sealed class ExternalMsg : CheckersMsg() {
    data class CellSelected(val cell: Cell) : ExternalMsg()
}

sealed class InternalMsg : CheckersMsg() {
    data class ShowFailure(val failure: Failure) : InternalMsg()
    data class ApplyGameState(val gameState: GameState) : InternalMsg()
}

sealed class CheckersEffect {
    data class PerformUserAction(
        val gameState: GameState.Continues,
        val gameType: GameType,
        val startPiece: Cell.Piece,
        val finishCell: Cell.Empty
    ) : CheckersEffect()
    
    data class CalculateNextAiMove(val gameState: GameState.Continues) : CheckersEffect()
}

fun externalUpdater() = Update<CheckersState, ExternalMsg, CheckersEffect> { msg, state ->
    when (msg) {
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
                state.copy(gameState = msg.gameState, startPiece = null) to setOfNotNull(
                    if (
                        msg.gameState is GameState.Continues
                        && activePlayer(msg.gameState.activePlayer, state.gameType) == ActivePlayer.AI
                    ) {
                        CheckersEffect.CalculateNextAiMove(msg.gameState)
                    } else {
                        null
                    }
                )
        }
    } else {
        state to emptySet()
    }
}
