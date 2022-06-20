package com.kubsu.checkers.feature

import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.game.GameState
import kotlinx.serialization.Serializable

data class CheckersState(
    val gameType: GameType,
    val gameState: GameState,
    val startPiece: Cell.Piece?,
    val failure: Failure?
)

@Serializable
enum class GameType {
    HumanVsAi,
    HumanVsHuman,
    AiVsAi,
}

enum class ActivePlayer { Human, AI }

fun activePlayer(playerColor: CellColor, gameType: GameType): ActivePlayer =
    when (gameType) {
        GameType.HumanVsHuman -> ActivePlayer.Human
        GameType.AiVsAi -> ActivePlayer.AI
        GameType.HumanVsAi ->
            when (playerColor) {
                CellColor.Dark -> ActivePlayer.AI
                CellColor.Light -> ActivePlayer.Human
            }
    }
