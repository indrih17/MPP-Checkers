package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.minmax.*
import com.kubsu.checkers.completableFold
import com.kubsu.checkers.difference
import com.kubsu.checkers.functions.move.ai.getAllMovesSequence
import kotlin.math.*

fun Board.minimax(
    current: Cell.Piece,
    depth: Int = 4,
    data: MinMaxData = MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE),
    player: MaximizingPlayer = MaximizingPlayer.Self(current.color)
): Node =
    if (depth == 0) {
        createNode(current, depth, player)
    } else {
        var minMaxData = data
        getAllMovesSequence(current)
            .map { (board, cell) ->
                board.minimax(cell, depth - 1, minMaxData, player.enemy())
            }
            .completableFold(initial = null) { acc, new, completeFold ->
                println("Depth = $depth, acc = $acc")
                println("Depth = $depth, new = $new")
                (acc?.update(new) ?: new.create(current, player))
                    .also {
                        minMaxData = player.updateMinMaxData(it.eval, minMaxData)
                        println("Depth = $depth, minMaxData = $minMaxData\n")
                        if (isNeedStop(minMaxData)) completeFold()
                    }
            }
            ?: createNode(current, depth, player)
    }

private fun Board.createNode(current: Cell.Piece, depth: Int, player: MaximizingPlayer): Node =
    Node(current, null, player, evaluation(current, depth, player))

internal fun Board.evaluation(current: Cell.Piece, depth: Int, player: MaximizingPlayer): Int {
    val manCells = filterIsInstance<Cell.Piece.Man>()
    val kingCells = filterIsInstance<Cell.Piece.King>()
    val mans = manCells.count(current::isSelf) - manCells.count(current::isEnemy)
    val kings = kingCells.count(current::isSelf) - kingCells.count(current::isEnemy)
    return mans + 3 * kings - if (isBadMove(current, player)) 2 else 0
}

private fun Board.isBadMove(cell: Cell.Piece, player: MaximizingPlayer): Boolean =
    if (player is MaximizingPlayer.Self)
        increasesSequence
            .mapNotNull { getOrNull(cell, it) }
            .any { it is Cell.Piece && it isEnemy cell }
    else
        false

internal fun Node.update(newElem: Node): Node {
    val newEval = player.minMaxEval(eval, newElem.eval)
    return copy(
        eval = newEval,
        finishCell = if (newEval != eval) newElem.startCell.toEmpty() else finishCell
    )
}

internal fun Node.create(cell: Cell.Piece, player: MaximizingPlayer): Node =
    copy(
        startCell = cell,
        finishCell = startCell.toEmpty(),
        player = player
    )

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