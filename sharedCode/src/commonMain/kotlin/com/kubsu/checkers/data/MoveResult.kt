package com.kubsu.checkers.data

data class MoveResult(
    val board: Board,
    val score: Score,
    val nextMove: CellColor
)
