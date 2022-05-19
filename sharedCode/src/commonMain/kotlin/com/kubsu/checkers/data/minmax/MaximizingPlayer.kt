package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor

/**
 * Player to search for the best/worst move.
 * @param color The color of the player's checkers.
 */
sealed class MaximizingPlayer(open val color: CellColor) {
    data class Enemy(override val color: CellColor) : MaximizingPlayer(color)
    data class Self(override val color: CellColor) : MaximizingPlayer(color)
}

/** @return the enemy's player for [this]. */
fun MaximizingPlayer.enemy(): MaximizingPlayer =
    if (this is MaximizingPlayer.Enemy)
        MaximizingPlayer.Self(color.enemy)
    else
        MaximizingPlayer.Enemy(color.enemy)

/** @return true if the checker belongs to the player. */
fun MaximizingPlayer.isSelfPiece(cell: Cell.Piece): Boolean =
    color == cell.color

/** @return true if the checker belongs to the enemy. */
fun MaximizingPlayer.isEnemyPiece(cell: Cell.Piece): Boolean =
    color != cell.color