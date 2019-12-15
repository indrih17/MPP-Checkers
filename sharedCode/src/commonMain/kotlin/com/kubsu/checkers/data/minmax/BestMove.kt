package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Cell

data class BestMove(
    val current: Cell,
    val destination: Cell,
    val player: MaximizingPlayer,
    val eval: Int
)

fun BestMove.update(newElem: BestMove, currentCell: Cell): BestMove {
    val newEval = player.minMaxEval(eval, newElem.eval)
    return BestMove(
        current = currentCell,
        destination = if (newEval != eval) newElem.current else current,
        eval = newEval,
        player = player.enemy()
    )
}

fun BestMove.create(currentCell: Cell): BestMove =
    copy(current = currentCell, destination = current, player = player.enemy())
