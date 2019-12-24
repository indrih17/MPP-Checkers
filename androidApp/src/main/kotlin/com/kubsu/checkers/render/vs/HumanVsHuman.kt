package com.kubsu.checkers.render.vs

import com.kubsu.checkers.GameType
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.functions.move.human.makeMove
import com.kubsu.checkers.render.CommonData
import com.kubsu.checkers.render.clear
import com.kubsu.checkers.render.render

internal fun CommonData.updateGame(gameType: GameType.HumanVsHuman) {
    tableLayout.clear()
    updateData(userGameState.gameState)
    val gameResult = userGameState.gameState.gameResultOrNull()
    if (gameResult != null)
        endGame(gameResult)
    else
        render(
            tableLayout = tableLayout,
            onClick = { clickedCell ->
                userGameState.makeMove(clickedCell).fold(
                    ifLeft = onFail,
                    ifRight = {
                        copy(userGameState = it).updateGame(gameType)
                    }
                )
            }
        )
}