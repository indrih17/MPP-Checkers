package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.minmax.*
import com.kubsu.checkers.completableFold
import com.kubsu.checkers.functions.isGameOver
import com.kubsu.checkers.functions.move.ai.getAllMovesSequence

fun GameState.minimax(
    depth: Int,
    startCell: Cell.Piece,
    current: Cell = startCell,
    data: MinMaxData = MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE),
    player: MaximizingPlayer = MaximizingPlayer.Self(activePlayerColor)
): BestMove? =
    if (depth == 0 || isGameOver()) // TODO is game over in position
        board.createBestMove(current, data, player)
    else
        getAllMovesSequence(startCell, current)
            .map { minimax(depth - 1, startCell, it, data, player.enemy()) }
            .filterNotNull()
            .completableFold(initial = null) { acc, new, completeFold ->
                val bestMove = acc?.update(new) ?: new.create(current)
                val minMaxData = player.minMaxDataOrNull(new.eval, new.minMaxData)
                if (minMaxData != null)
                    bestMove.copy(minMaxData = minMaxData).also { completeFold() }
                else
                    bestMove
            }

private fun Board.createBestMove(cell: Cell, data: MinMaxData, player: MaximizingPlayer): BestMove =
    BestMove(cell, cell, player, evaluation(cell, player), data)

private fun Board.evaluation(cell: Cell, player: MaximizingPlayer): Int {
    val manCells = filterIsInstance<Cell.Piece.Man>()
    val kingCells = filterIsInstance<Cell.Piece.King>()
    val mans = manCells.count(player::isSelf) - manCells.count(player::isEnemy)
    val kings = kingCells.count(player::isSelf) - kingCells.count(player::isEnemy)
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