package feature

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.move.ai.aiMove
import com.kubsu.checkers.functions.move.human.move
import family.amma.keemun.Dispatch
import family.amma.keemun.EffectHandler

fun checkersEffectHandler() = EffectHandler<CheckersEffect, CheckersMsg> { effect, dispatch: Dispatch<InternalMsg> ->
    when (effect) {
        is CheckersEffect.PerformUserAction ->
            dispatch(
                when (effect.startPiece) {
                    is Cell.Piece.Man -> effect.gameState.move(effect.startPiece, effect.finishCell)
                    is Cell.Piece.King -> effect.gameState.move(effect.startPiece, effect.finishCell)
                }.fold(
                    ifLeft = InternalMsg::ShowFailure,
                    ifRight = InternalMsg::ApplyGameState
                )
            )

        is CheckersEffect.CalculateNextAiMove ->
            dispatch(InternalMsg.ApplyGameState(aiMove(effect.gameState)))
    }
}
