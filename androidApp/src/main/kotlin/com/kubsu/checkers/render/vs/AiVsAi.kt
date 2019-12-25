package com.kubsu.checkers.render.vs

import com.kubsu.checkers.GameType
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.ai.makeAIMove
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.render.CommonData
import com.kubsu.checkers.render.clear
import com.kubsu.checkers.render.render
import com.kubsu.checkers.render.updateCommonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal fun CommonData.updateGame(gameType: GameType.AiVsAi) {
    tableLayout.clear()
    val gameState = userGameState.gameState
    updateData(gameState)
    val gameResult = gameState.gameResultOrNull()
    if (gameResult != null) {
        endGame(gameResult)
    } else {
        render(tableLayout)
        scope.launch(Dispatchers.Main) {
            makeAIMove(gameState).fold(
                ifLeft = onFail,
                ifRight = {
                    updateCommonData(it).updateGame(gameType)
                }
            )
        }
    }
}