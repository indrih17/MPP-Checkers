package com.kubsu.checkers.data

import kotlin.math.max
import kotlin.math.min

data class MinMaxData(val depth: Int, val alpha: Int, val beta: Int) {
    fun decrementDepth(): MinMaxData = copy(depth = depth - 1)
}

sealed class MaximizingPlayer(val color: CellColor) {
    class Enemy(color: CellColor) : MaximizingPlayer(color)
    class Self(color: CellColor) : MaximizingPlayer(color)

    fun enemy(): MaximizingPlayer =
        if (this is Enemy) Self(color.enemy()) else Enemy(color.enemy())

    fun isEnemy(cell: Cell.Piece): Boolean =
        if (this is Enemy) color == cell.color else color != cell.color

    fun isSelf(cell: Cell.Piece) = !isEnemy(cell)
}

data class BestMove(
    val current: Cell,
    val destination: Cell,
    val player: MaximizingPlayer,
    val eval: Int
) {
    fun update(newElem: BestMove, current: Cell): BestMove {
        val newEval = if (player is MaximizingPlayer.Self) max(eval, newElem.eval) else min(eval, newElem.eval)
        return copy(
            current = current,
            destination = if (newEval != eval) newElem.destination else destination,
            eval = newEval,
            player = player.enemy()
        )
    }

    fun create(currentCell: Cell): BestMove =
        copy(current = currentCell, player = player.enemy())
}
