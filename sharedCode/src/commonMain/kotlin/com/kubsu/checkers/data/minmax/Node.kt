package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Cell

data class Node(
    val startCell: Cell.Piece,
    val finishCell: Cell.Empty?,
    val player: MaximizingPlayer,
    val minMaxData: MinMaxData,
    val eval: Int
)