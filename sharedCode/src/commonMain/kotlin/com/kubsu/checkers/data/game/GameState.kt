package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.CellColor

data class GameState(
    val board: Board,
    val score: Score,
    val activePlayer: ActivePlayer
)

data class ActivePlayer(
    val color: CellColor,
    val simpleMoves: Int
)
