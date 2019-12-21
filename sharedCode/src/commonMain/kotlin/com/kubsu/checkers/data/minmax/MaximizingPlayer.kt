package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.entities.enemy
import kotlin.math.max
import kotlin.math.min

sealed class MaximizingPlayer(open val color: CellColor) {
    data class Enemy(override val color: CellColor) : MaximizingPlayer(color)
    data class Self(override val color: CellColor) : MaximizingPlayer(color)
}

fun MaximizingPlayer.minMaxEval(eval1: Int, eval2: Int): Int =
    if (this is MaximizingPlayer.Self) max(eval1, eval2) else min(eval1, eval2)

fun MaximizingPlayer.minMaxDataOrNull(eval: Int, data: MinMaxData): MinMaxData? =
    if (this is MaximizingPlayer.Self) {
        val newAlpha = max(data.alpha, eval)
        if (data.beta <= newAlpha)
            data.copy(alpha = newAlpha)
        else
            null
    } else {
        val newBeta = min(data.beta, eval)
        if (newBeta <= data.alpha)
            data.copy(beta = newBeta)
        else
            null
    }

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