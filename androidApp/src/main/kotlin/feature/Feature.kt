package feature

import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.functions.createGameState
import family.amma.keemun.InitFeature
import family.amma.keemun.feature.FeatureParams

typealias CheckersFeatureParams = FeatureParams<CheckersState, CheckersMsg, CheckersEffect>

fun checkersFeatureParams(): CheckersFeatureParams {
    val internalUpdater = internalUpdater()
    val externalUpdater = externalUpdater()
    return FeatureParams(
        init = InitFeature { previous: CheckersState? ->
            val state = previous ?: CheckersState(
                gameType = GameType.HumanVsHuman,
                gameState = createGameState(playerColor = CellColor.Light),
                startPiece = null,
                failure = null,
            )
            state to setOfNotNull(
                if (
                    state.gameState is GameState.Continues
                    && activePlayer(state.gameState.activePlayer, state.gameType) == ActivePlayer.AI
                ) {
                    CheckersEffect.CalculateNextAiMove(state.gameState)
                } else {
                    null
                }
            )
        },
        update = { msg, state ->
            when (msg) {
                is InternalMsg -> internalUpdater(msg, state)
                is ExternalMsg -> externalUpdater(msg, state)
            }
        },
        effectHandler = checkersEffectHandler()
    )
}
