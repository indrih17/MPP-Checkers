package com.kubsu.checkers.render.vs

import com.kubsu.checkers.GameType
import com.kubsu.checkers.data.game.UserGameState
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.ai.makeAIMove
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.functions.move.human.makeMove
import com.kubsu.checkers.render.CommonData
import com.kubsu.checkers.render.clear
import com.kubsu.checkers.render.render
import com.kubsu.checkers.render.updateCommonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class ActivePlayer {
    object Human : ActivePlayer() {
        val enemy = Ai
    }
    object Ai : ActivePlayer() {
        val enemy = Human
    }
}

internal fun CommonData.updateGame(
    gameType: GameType.HumanVsAi,
    activePlayer: ActivePlayer
) {
    tableLayout.clear()
    updateData(userGameState.gameState)
    userGameState
        .gameState
        .gameResultOrNull()
        ?.let(endGame)
        ?: run {
            when (activePlayer) {
                is ActivePlayer.Human ->
                    makeMove(gameType, activePlayer)

                is ActivePlayer.Ai ->
                    makeMove(gameType, activePlayer)
            }
        }
}

internal fun CommonData.makeMove(
    gameType: GameType.HumanVsAi,
    activePlayer: ActivePlayer.Human
) {
    render(
        tableLayout = tableLayout,
        onClick = { clickedCell ->
            userGameState.makeMove(clickedCell).fold(
                ifLeft = onFail,
                ifRight = {
                    if (userGameState.isUserMadeMove(it))
                        copy(userGameState = it).updateGame(gameType, activePlayer.enemy)
                    else
                        copy(userGameState = it).updateGame(gameType, activePlayer)
                }
            )
        }
    )
}

private fun UserGameState.isUserMadeMove(userGameState: UserGameState): Boolean =
    userGameState.gameState.activePlayerColor != gameState.activePlayerColor

private fun CommonData.makeMove(
    gameType: GameType.HumanVsAi,
    activePlayer: ActivePlayer.Ai
) {
    val gameState = userGameState.gameState
    render(tableLayout)
    scope.launch(Dispatchers.Main) {
        makeAIMove(gameState).fold(
            ifLeft = onFail,
            ifRight = {
                updateCommonData(it).updateGame(gameType, activePlayer.enemy)
            }
        )
    }
}