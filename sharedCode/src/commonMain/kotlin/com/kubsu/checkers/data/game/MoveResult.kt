package com.kubsu.checkers.data.game

import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.CellColor

data class MoveResult(
    val board: Board,
    val score: Score,
    val nextMove: CellColor,
    val moveType: MoveType
)

enum class MoveType {
    SimpleMove,
    Attack
}