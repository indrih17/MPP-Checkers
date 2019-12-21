package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.functions.createBoard

data class GameState(
    val board: Board,
    val score: Score,
    val activePlayerColor: CellColor,
    val movesWithoutAttacks: Int
) {
    constructor(boardSize: Int, playerColor: CellColor) : this(
        board = createBoard(boardSize),
        score = Score(),
        activePlayerColor = playerColor,
        movesWithoutAttacks = 0
    )
}