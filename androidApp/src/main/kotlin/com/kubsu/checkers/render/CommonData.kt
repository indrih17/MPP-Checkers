package com.kubsu.checkers.render

import android.widget.TableLayout
import com.kubsu.checkers.GameType
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.UserGameState
import com.kubsu.checkers.functions.GameResult
import com.kubsu.checkers.render.vs.ActivePlayer
import com.kubsu.checkers.render.vs.updateGame
import kotlinx.coroutines.CoroutineScope

fun startGame(common: CommonData, gameType: GameType) =
    when (gameType) {
        is GameType.HumanVsHuman ->
            common.updateGame(gameType)
        is GameType.HumanVsAi ->
            common.updateGame(gameType, ActivePlayer.Human)
        is GameType.AiVsAi ->
            common.updateGame(gameType)
    }


data class CommonData(
    val tableLayout: TableLayout,
    val scope: CoroutineScope,
    val userGameState: UserGameState,
    val updateData: (GameState) -> Unit,
    val onFail: (Failure) -> Unit,
    val endGame: (GameResult) -> Unit
)

fun CommonData.updateCommonData(gameState: GameState): CommonData =
    copy(userGameState = UserGameState(gameState = gameState, startCell = null))
