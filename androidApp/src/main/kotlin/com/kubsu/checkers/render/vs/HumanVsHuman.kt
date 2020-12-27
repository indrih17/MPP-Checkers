package com.kubsu.checkers.render.vs

import com.kubsu.checkers.GameType
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.functions.move.human.makeMove
import com.kubsu.checkers.render.UiState
import com.kubsu.checkers.render.clear
import com.kubsu.checkers.render.render

internal fun UiState.updateGame(gameType: GameType.HumanVsHuman) {
    updateData(userState.gameState)
    val gameResult = userState.gameState.gameResultOrNull()
    if (gameResult != null) {
        endGame(gameResult)
    } else {
        tableLayout.clear()
        render(
            tableLayout = tableLayout,
            onClick = { clickedCell ->
                userState.makeMove(clickedCell).fold(
                    ifLeft = onFail,
                    ifRight = { copy(userState = it).updateGame(gameType) }
                )
            }
        )
    }
}