package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.minmax.*
import com.kubsu.checkers.completableFold
import com.kubsu.checkers.completableNullableFold
import com.kubsu.checkers.functions.move.ai.getAllMovesSequence
import com.kubsu.checkers.functions.move.ai.getAvailableCellsSequence

fun Board.minimax(
    startCell: Cell.Piece,
    current: Cell = startCell,
    depth: Int = 4,
    data: MinMaxData = MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE),
    player: MaximizingPlayer = MaximizingPlayer.Self(startCell.color)
): BestMove =
    if (depth == 0 /*|| isGameOver()*/) // TODO is game over in position
        createBestMove(startCell, current, data, player)
    else
        getAllMovesSequence(startCell, current)
            .map { minimax(startCell, it, depth - 1, data, player.enemy()) }
            .completableFold(
                initial = createBestMove(startCell, current, data, player)
            ) { acc, new, completeFold ->
                val bestMove = acc.update(new)
                val minMaxData = player.minMaxDataOrNull(new.eval, new.minMaxData)
                if (minMaxData != null)
                    bestMove.copy(minMaxData = minMaxData).also { completeFold() }
                else
                    bestMove
            }

private fun Board.createBestMove(startCell: Cell.Piece, cell: Cell, data: MinMaxData, player: MaximizingPlayer): BestMove =
    BestMove(cell, cell, player, data, evaluation(startCell, cell, player))

//TODO сделать норм оценочную функцию, которая не будет возвращать для запертых и свободных одно и то же значение
private fun Board.evaluation(startCell: Cell.Piece, cell: Cell, player: MaximizingPlayer): Int {
    val manCells = filterIsInstance<Cell.Piece.Man>()
    val kingCells = filterIsInstance<Cell.Piece.King>()
    val mans = manCells.count(player::isSelf) - manCells.count(player::isEnemy)
    val kings = kingCells.count(player::isSelf) - kingCells.count(player::isEnemy)
    return mans + 3 * kings - if (isBadMove(cell, player)) 2 else 0
}

private fun Board.isBadMove(cell: Cell, player: MaximizingPlayer): Boolean =
    if (player is MaximizingPlayer.Self) {
        val left = getLeftCellOrNull(cell, player)
        val right = getRightCellOrNull(cell, player)
        (left is Cell.Piece && left.color != player.color) || (right is Cell.Piece && right.color != player.color)
    } else {
        false
    }

private fun Board.getLeftCellOrNull(cell: Cell, player: MaximizingPlayer.Self): Cell? =
    when (player.color) {
        is CellColor.Light -> getOrNull(cell.row - 1, cell.column - 1)
        is CellColor.Dark -> getOrNull(cell.row + 1, cell.column + 1)
    }

private fun Board.getRightCellOrNull(cell: Cell, player: MaximizingPlayer.Self): Cell? =
    when (player.color) {
        is CellColor.Light -> getOrNull(cell.row - 1, cell.column + 1)
        is CellColor.Dark -> getOrNull(cell.row + 1, cell.column - 1)
    }