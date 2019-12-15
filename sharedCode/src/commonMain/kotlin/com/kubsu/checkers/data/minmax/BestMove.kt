package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Cell
import kotlin.math.max
import kotlin.math.min

data class BestMove(
    val current: Cell,
    val destination: Cell,
    val player: MaximizingPlayer,
    val eval: Int
)

fun BestMove.update(newElem: BestMove, current: Cell): BestMove {
    val newEval = if (player is MaximizingPlayer.Self) max(eval, newElem.eval) else min(eval, newElem.eval)
    return copy(
        current = current,
        destination = if (newEval != eval) newElem.destination else destination,
        eval = newEval,
        player = player.enemy()
    )
}

fun BestMove.create(currentCell: Cell): BestMove =
    copy(destination = current, current = currentCell, player = player.enemy())
