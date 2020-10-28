package com.kubsu.checkers.render.vs

import com.kubsu.checkers.GameType
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.ai.makeAIMove
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.render.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal fun UiState.updateGame(gameType: GameType.AiVsAi) {
    tableLayout.clear()
    val gameState = userState.gameState
    updateData(gameState)
    val gameResult = gameState.gameResultOrNull()
    if (gameResult != null) {
        endGame(gameResult)
    } else {
        render(tableLayout)
        scope.launch(Dispatchers.Main) {
            makeAIMove(gameState).fold(
                ifLeft = onFail,
                ifRight = { copy(userState = it).updateGame(gameType) }
            )
        }
    }
}