package com.kubsu.checkers.render.vs

import com.kubsu.checkers.GameType
import com.kubsu.checkers.data.game.UserState
import com.kubsu.checkers.fold
import com.kubsu.checkers.functions.gameResultOrNull
import com.kubsu.checkers.functions.move.ai.makeAIMove
import com.kubsu.checkers.functions.move.human.makeMove
import com.kubsu.checkers.render.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class ActivePlayer {
    abstract val enemy: ActivePlayer

    object Human : ActivePlayer() {
        override val enemy = Ai
    }

    object Ai : ActivePlayer() {
        override val enemy = Human
    }
}

internal fun UiState.updateGame(
    gameType: GameType.HumanVsAi,
    activePlayer: ActivePlayer
) {
    updateData(userState.gameState)
    userState
        .gameState
        .gameResultOrNull()
        ?.let(endGame)
        ?: run {
            tableLayout.clear()
            when (activePlayer) {
                is ActivePlayer.Human -> makeMove(gameType, activePlayer)
                is ActivePlayer.Ai -> makeMove(gameType, activePlayer)
            }
        }
}

internal fun UiState.makeMove(
    gameType: GameType.HumanVsAi,
    activePlayer: ActivePlayer.Human
) {
    render(
        tableLayout = tableLayout,
        onClick = { clickedCell ->
            userState.makeMove(clickedCell).fold(
                ifLeft = onFail,
                ifRight = {
                    if (userState.isUserMadeMove(userState = it)) {
                        copy(userState = it).updateGame(gameType, activePlayer.enemy)
                    } else {
                        copy(userState = it).updateGame(gameType, activePlayer)
                    }
                }
            )
        }
    )
}

internal fun UserState.isUserMadeMove(userState: UserState): Boolean =
    userState.gameState.activePlayer != gameState.activePlayer

internal fun UiState.makeMove(gameType: GameType.HumanVsAi, activePlayer: ActivePlayer.Ai) {
    render(tableLayout)
    scope.launch(Dispatchers.Main) {
        makeAIMove(userState.gameState).fold(
            ifLeft = onFail,
            ifRight = { copy(userState = it).updateGame(gameType, activePlayer.enemy) }
        )
    }
}
