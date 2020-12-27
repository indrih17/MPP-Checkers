package com.kubsu.checkers.render.vs

import com.kubsu.checkers.GameType
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.functions.move.ai.makeAIMove
import com.kubsu.checkers.render.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal fun UiState.updateGame(gameType: GameType.AiVsAi) {
    val gameState = userState.gameState
    updateData(gameState)
    val gameResult = gameState.gameResultOrNull()
    if (gameResult != null) {
        endGame(gameResult)
    } else {
        tableLayout.clear()
        render(tableLayout)
        scope.launch(Dispatchers.Main) {
            makeAIMove(gameState).fold(
                ifLeft = onFail,
                ifRight = { copy(userState = it).updateGame(gameType) }
            )
        }
    }
}