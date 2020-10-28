package com.kubsu.checkers

import com.kubsu.checkers.render.UiState
import com.kubsu.checkers.render.vs.ActivePlayer
import com.kubsu.checkers.render.vs.updateGame

fun startGame(uiState: UiState, gameType: GameType) =
    when (gameType) {
        is GameType.HumanVsHuman ->
            uiState.updateGame(gameType)
        is GameType.HumanVsAi ->
            uiState.updateGame(gameType, ActivePlayer.Human)
        is GameType.AiVsAi ->
            uiState.updateGame(gameType)
    }
