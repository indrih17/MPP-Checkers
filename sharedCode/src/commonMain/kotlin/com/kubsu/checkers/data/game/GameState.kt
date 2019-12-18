package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor

data class GameState(
    val board: Board,
    val score: Score,
    val activePlayerColor: CellColor,
    val selectedCell: Cell.Piece?,
    val movesWithoutAttacks: Int
)