package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.minmax.*
import com.kubsu.checkers.completableFold
import com.kubsu.checkers.functions.move.ai.getAllMovesSequence
import kotlin.math.max
import kotlin.math.min

fun Board.minimax(
    current: Cell.Piece,
    depth: Int = 4,
    data: MinMaxData = MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE),
    player: MaximizingPlayer = MaximizingPlayer.Self(current.color)
): Node =
    if (depth == 0)
        node(current, data, player)
    else
        getAllMovesSequence(current)
            .map { (board, cell) -> board.minimax(cell, depth - 1, data, player.enemy()) }
            .completableFold(initial = null) { acc, new, completeFold ->
                (acc?.update(new) ?: new.create(current))
                    .also { if (isNeedStop(it.minMaxData)) completeFold() }
            }
            ?: node(current, data, player)

private fun Board.node(current: Cell.Piece, data: MinMaxData, player: MaximizingPlayer): Node =
    Node(current, null, player, data, evaluation(current, player))

private fun Board.evaluation(current: Cell.Piece, player: MaximizingPlayer): Int {
    val manCells = filterIsInstance<Cell.Piece.Man>()
    val kingCells = filterIsInstance<Cell.Piece.King>()
    val mans = manCells.count(player::isSelf) - manCells.count(player::isEnemy)
    val kings = kingCells.count(player::isSelf) - kingCells.count(player::isEnemy)
    return mans + 3 * kings - if (isBadMove(current, player)) 2 else 0
}

private fun Board.isBadMove(cell: Cell.Piece, player: MaximizingPlayer): Boolean =
    if (player is MaximizingPlayer.Self)
        increasesSequence
            .mapNotNull { getOrNull(cell, it) }
            .any { it is Cell.Piece && it.isEnemy(player.color) }
    else
        false

internal fun Node.update(newElem: Node): Node {
    val newEval = player.minMaxEval(eval, newElem.eval)
    return copy(
        eval = newEval,
        minMaxData = player.updateMinMaxData(newEval, newElem.minMaxData),
        finishCell = if (newEval != eval) newElem.startCell.toEmpty() else finishCell
    )
}

internal fun Node.create(cell: Cell.Piece): Node =
    copy(startCell = cell, finishCell = startCell.toEmpty(), player = player.enemy())

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