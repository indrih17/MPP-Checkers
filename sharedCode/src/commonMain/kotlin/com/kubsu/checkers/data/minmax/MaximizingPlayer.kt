package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.entities.enemy
import kotlin.math.max
import kotlin.math.min

sealed class MaximizingPlayer(val color: CellColor) {
    class Enemy(color: CellColor) : MaximizingPlayer(color)
    class Self(color: CellColor) : MaximizingPlayer(color)
}

fun MaximizingPlayer.minMaxEval(eval1: Int, eval2: Int): Int =
    if (this is MaximizingPlayer.Self) max(eval1, eval2) else min(eval1, eval2)

fun MaximizingPlayer.minMaxCondition(eval: Int, alpha: Int, beta: Int): Boolean =
    if (this is MaximizingPlayer.Self)
        max(alpha, eval) < beta
    else
        min(beta, eval) > alpha

fun MaximizingPlayer.enemy(): MaximizingPlayer =
    if (this is MaximizingPlayer.Enemy)
        MaximizingPlayer.Self(color.enemy())
    else
        MaximizingPlayer.Enemy(color.enemy())

fun MaximizingPlayer.isEnemy(cell: Cell.Piece): Boolean =
    if (this is MaximizingPlayer.Enemy)
        color == cell.color
    else
        color != cell.color

fun MaximizingPlayer.isSelf(cell: Cell.Piece) =
    !isEnemy(cell)