package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.Cell

/**
 * The state of the game and the user's choice.
 * @param gameState [GameState].
 * @param startPiece The piece selected by the player.
 */
data class UserState(val gameState: GameState, val startPiece: Cell.Piece?)