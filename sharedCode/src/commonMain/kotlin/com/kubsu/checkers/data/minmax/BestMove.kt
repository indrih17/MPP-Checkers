package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Cell

data class BestMove(
    val startCell: Cell,
    val finishCell: Cell,
    val player: MaximizingPlayer,
    val minMaxData: MinMaxData,
    val eval: Int
)

fun BestMove.update(newElem: BestMove): BestMove {
    val newEval = player.minMaxEval(eval, newElem.eval)
    return copy(
        eval = newEval,
        minMaxData = newElem.minMaxData,
        finishCell = when {
            startCell == finishCell -> newElem.startCell
            newEval != eval -> newElem.startCell
            else -> finishCell
        }
    )
}

fun BestMove.create(cell: Cell): BestMove =
    copy(startCell = cell, finishCell = startCell, player = player.enemy())
