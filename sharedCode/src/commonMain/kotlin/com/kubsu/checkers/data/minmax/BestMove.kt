package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Cell

data class BestMove(
    val startCell: Cell,
    val finishCell: Cell,
    val player: MaximizingPlayer,
    val eval: Int,
    val minMaxData: MinMaxData
)

fun BestMove.update(newElem: BestMove): BestMove {
    val newEval = player.minMaxEval(eval, newElem.eval)
    return copy(
        eval = newEval,
        finishCell = if (newEval != eval) newElem.startCell else startCell
    )
}

fun BestMove.create(cell: Cell): BestMove =
    copy(startCell = cell, finishCell = startCell, player = player.enemy())
