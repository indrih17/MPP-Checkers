package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.minmax.*
import com.kubsu.checkers.completableFold
import com.kubsu.checkers.functions.move.ai.getAllMovesSequence
import kotlin.math.max
import kotlin.math.min

fun Board.minimax(
    startCell: Cell.Piece,
    current: Cell = startCell,
    depth: Int = 4,
    data: MinMaxData = MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE),
    player: MaximizingPlayer = MaximizingPlayer.Self(startCell.color)
): BestMove =
    if (depth == 0)
        bestMove(current, data, player)
    else
        getAllMovesSequence(startCell, current)
            .map { (board, cell) ->
                board.minimax(startCell, cell, depth - 1, data, player.enemy())
            }
            .completableFold(initial = null) { acc, new, completeFold ->
                (acc?.update(new) ?: new.create(current))
                    .also { if (isNeedStop(it.minMaxData)) completeFold() }
            }
            ?: bestMove(current, data, player)

private fun Board.bestMove(cell: Cell, data: MinMaxData, player: MaximizingPlayer): BestMove =
    BestMove(cell, cell, player, data, evaluation(cell, player))

private fun Board.evaluation(cell: Cell, player: MaximizingPlayer): Int {
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

internal fun BestMove.update(newElem: BestMove): BestMove {
    val newEval = player.minMaxEval(eval, newElem.eval)
    return copy(
        eval = newEval,
        minMaxData = player.updateMinMaxData(newEval, newElem.minMaxData),
        finishCell = if (newEval != eval) newElem.startCell else finishCell
    )
}

internal fun BestMove.create(cell: Cell): BestMove =
    copy(startCell = cell, finishCell = startCell, player = player.enemy())

internal val MaximizingPlayer.defaultEval: Int
    inline get() = if (this is MaximizingPlayer.Self) Int.MIN_VALUE else Int.MAX_VALUE

internal fun MaximizingPlayer.minMaxEval(eval1: Int, eval2: Int): Int =
    if (this is MaximizingPlayer.Self) max(eval1, eval2) else min(eval1, eval2)

internal fun MaximizingPlayer.updateMinMaxData(eval: Int, data: MinMaxData): MinMaxData =
    if (this is MaximizingPlayer.Self)
        data.copy(alpha = max(data.alpha, eval))
    else
        data.copy(beta = min(data.beta, eval))

internal fun isNeedStop(data: MinMaxData): Boolean =
    data.beta <= data.alpha