package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.entities.enemy

sealed class MaximizingPlayer(open val color: CellColor) {
    data class Enemy(override val color: CellColor) : MaximizingPlayer(color)
    data class Self(override val color: CellColor) : MaximizingPlayer(color)
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