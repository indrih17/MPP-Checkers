package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Cell

data class BestMove(
    val startCell: Cell,
    val finishCell: Cell,
    val player: MaximizingPlayer,
    val minMaxData: MinMaxData,
    val eval: Int
)