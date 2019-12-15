package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.minmax.*
import com.kubsu.checkers.foldT
import com.kubsu.checkers.functions.move.get.getAllMovesSequence

fun Board.minimax(start: Cell.Piece, current: Cell, data: MinMaxData, player: MaximizingPlayer): BestMove? =
    if (data.depth == 0)
        BestMove(current = current, destination = current, player = player, eval = evaluation(current, player))
    else
        getAllMovesSequence(start, current)
            .map { minimax(start, it, data.decrementDepth(), player.enemy()) }
            .filterNotNull()
            .takeWhile { player.minMaxCondition(it.eval, data.alpha, data.beta) }
            .foldT(initial = null) { acc, new ->
                acc?.update(new, current) ?: new.create(current)
            }

private fun Board.evaluation(cell: Cell, player: MaximizingPlayer): Int {
    val manCells = filterIsInstance<Cell.Piece.Man>()
    val kingCells = filterIsInstance<Cell.Piece.King>()
    val mans = manCells.count(player::isEnemy) - manCells.count(player::isSelf)
    val kings = kingCells.count(player::isEnemy) - kingCells.count(player::isSelf)
    return mans + 3 * kings - if (isBadMove(cell, player)) 2 else 0
}

private fun Board.isBadMove(cell: Cell, player: MaximizingPlayer): Boolean =
    if (player is MaximizingPlayer.Self && thereIsSpaceForMove(cell, player)) {
        val left = getLeftCellOrNull(cell, player)
        val right = getRightCellOrNull(cell, player)
        (left is Cell.Piece && left.color != player.color) || (right is Cell.Piece && right.color != player.color)
    } else {
        false
    }

private fun Board.thereIsSpaceForMove(cell: Cell, player: MaximizingPlayer.Self): Boolean =
    if (player.color is CellColor.Dark) cell.row < lastIndex else cell.row > firstIndex

private fun Board.getLeftCellOrNull(cell: Cell, player: MaximizingPlayer.Self): Cell? =
    when (player.color) {
        is CellColor.Light -> if (cell.column < lastIndex) get(cell.row - 1, cell.column - 1) else null
        is CellColor.Dark -> if (cell.column > firstIndex) get(cell.row + 1, cell.column + 1) else null
    }

private fun Board.getRightCellOrNull(cell: Cell, player: MaximizingPlayer.Self): Cell? =
    when (player.color) {
        is CellColor.Light -> if (cell.column < lastIndex) get(cell.row - 1, cell.column + 1) else null
        is CellColor.Dark -> if (cell.column > firstIndex) get(cell.row + 1, cell.column - 1) else null
    }