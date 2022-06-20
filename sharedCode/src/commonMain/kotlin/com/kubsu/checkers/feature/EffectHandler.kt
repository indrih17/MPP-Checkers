package com.kubsu.checkers.feature

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.move.ai.aiMove
import com.kubsu.checkers.functions.move.human.move
import family.amma.keemun.Dispatch
import family.amma.keemun.EffectHandler

typealias CheckersEffectHandler = EffectHandler<CheckersEffect, CheckersMsg>

fun checkersEffectHandler(
    updateGameType: suspend (GameType) -> Unit
): CheckersEffectHandler = EffectHandler { effect, dispatch: Dispatch<InternalMsg> ->
    when (effect) {
        is CheckersEffect.PerformUserAction ->
            dispatch(
                when (effect.startPiece) {
                    is Cell.Piece.Man -> effect.gameState.move(effect.startPiece, effect.finishCell)
                    is Cell.Piece.King -> effect.gameState.move(effect.startPiece, effect.finishCell)
                }.fold(
                    ifLeft = InternalMsg::ShowFailure,
                    ifRight = { InternalMsg.ApplyGameState(gameState = it, effect.gameType) }
                )
            )

        is CheckersEffect.UpdateGameType ->
            updateGameType(effect.gameType)

        is CheckersEffect.CalculateNextAiMove ->
            dispatch(InternalMsg.ApplyGameState(gameState = aiMove(effect.gameState), gameType = effect.gameType))
    }
}
