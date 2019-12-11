package com.kubsu.checkers.data

data class GameState(
    val board: Board,
    val score: Score,
    val activePlayerColor: CellColor,
    val selectedCell: Cell.Piece?
)