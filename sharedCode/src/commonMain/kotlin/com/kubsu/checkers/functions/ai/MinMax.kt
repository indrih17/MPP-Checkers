package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.minmax.*
import com.kubsu.checkers.completableFold
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.functions.move.ai.getAllMovesSequence
import kotlin.math.*

internal fun GameState.getBestMoveOrNull(
    depth: Int = 4,
    player: MaximizingPlayer = MaximizingPlayer.Self(activePlayer.color),
    data: MinMaxData = MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE)
): Node? =
    board
        .getAllMovesSequence(player)
        .map { board -> Node(board, board.minimax(depth - 1, player.enemy(), data)) }
        .maxByOrNull(Node::eval)

internal fun Board.minimax(depth: Int, player: MaximizingPlayer, data: MinMaxData): Int =
    if (depth == 0) {
        evaluation(player)
    } else {
        var minMaxData = data
        getAllMovesSequence(player)
            .map { board -> board.minimax(depth - 1, player.enemy(), minMaxData) }
            .completableFold(initial = null) { old, new->
                player
                    .bestEval(old ?: new, new)
                    .let { best ->
                        minMaxData = player.updateMinMaxData(best, minMaxData)
                        best to isNeedStop(minMaxData)
                    }
            }
            ?: player.defaultEval
    }

internal fun Board.evaluation(player: MaximizingPlayer): Int {
    val pieceList = filterIsInstance<Cell.Piece>()
    val manCells = pieceList.filterIsInstance<Cell.Piece.Man>()
    val kingCells = pieceList.filterIsInstance<Cell.Piece.King>()
    val mans = 2 * manCells.count(player::isSameColor) - manCells.count(player::isEnemyColor)
    val kings = 2 * kingCells.count(player::isSameColor) - kingCells.count(player::isEnemyColor)
    val badMoves = pieceList.map { if (isBadMove(it)) 2 else 0 }.sum()
    return mans + 3 * kings - badMoves
}

internal fun Board.isBadMove(current: Cell.Piece): Boolean =
    increasesSequence.any { increase ->
        val cell = getOrNull(current, increase)
        cell is Cell.Piece && cell isEnemy current && getOrNull(cell, increase) is Cell.Piece
    }

internal val MaximizingPlayer.defaultEval: Int
    get() = if (this is MaximizingPlayer.Self) Int.MIN_VALUE else Int.MAX_VALUE

internal fun MaximizingPlayer.bestEval(eval1: Int, eval2: Int): Int =
    if (this is MaximizingPlayer.Self) max(eval1, eval2) else min(eval1, eval2)

internal fun MaximizingPlayer.updateMinMaxData(eval: Int, data: MinMaxData): MinMaxData =
    if (this is MaximizingPlayer.Self)
        data.copy(alpha = max(data.alpha, eval))
    else
        data.copy(beta = min(data.beta, eval))

internal fun isNeedStop(data: MinMaxData): Boolean =
    data.beta <= data.alpha