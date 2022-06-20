package com.kubsu.checkers.feature

import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.functions.createGameState
import family.amma.keemun.InitFeature
import family.amma.keemun.feature.FeatureParams

typealias CheckersFeatureParams = FeatureParams<CheckersState, CheckersMsg, CheckersEffect>

fun checkersFeatureParams(preEffect: suspend () -> GameType, effectHandler: CheckersEffectHandler): CheckersFeatureParams {
    val internalUpdater = internalUpdater()
    val externalUpdater = externalUpdater()
    return FeatureParams(
        init = InitFeature(
            preEffect = preEffect,
            init = { previous: CheckersState?, gameType ->
                val defaultState = CheckersState(
                    gameType = gameType,
                    gameState = createGameState(playerColor = CellColor.Light),
                    startPiece = null,
                    failure = null,
                )
                val state = if (previous != null) {
                    if (previous.gameType != gameType) defaultState else previous
                } else {
                    defaultState
                }
                state to setOfNotNull(calculateNextAiMoveOrNull(state.gameState, state.gameType))
            }
        ),
        update = { msg, state ->
            when (msg) {
                is InternalMsg -> internalUpdater(msg, state)
                is ExternalMsg -> externalUpdater(msg, state)
            }
        },
        effectHandler = effectHandler
    )
}

fun calculateNextAiMoveOrNull(gameState: GameState, gameType: GameType): CheckersEffect.CalculateNextAiMove? =
    if (
        gameState is GameState.Continues
        && activePlayer(gameState.activePlayer, gameType) == ActivePlayer.AI
    ) {
        CheckersEffect.CalculateNextAiMove(gameState, gameType)
    } else {
        null
    }
