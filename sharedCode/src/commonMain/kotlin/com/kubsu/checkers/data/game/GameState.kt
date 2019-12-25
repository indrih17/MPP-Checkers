package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.functions.createBoard

data class GameState(
    val board: Board,
    val score: Score,
    val activePlayerColor: CellColor,
    val simpleMoves: Int
) {
    constructor(playerColor: CellColor) : this(
        board = createBoard(),
        score = Score(0, 0),
        activePlayerColor = playerColor,
        simpleMoves = 0
    )
}