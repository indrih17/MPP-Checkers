package com.kubsu.checkers.functions.ai

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.minmax.*
import com.kubsu.checkers.completableFold
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.functions.move.ai.getAvailableCellsSequence
import kotlin.math.*

internal fun GameState.getBestMoveOrNull(
    depth: Int = 6,
    player: MaximizingPlayer = MaximizingPlayer.Self(activePlayer),
    data: MinMaxData = MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE)
): Node? =
    board
        .getAllMovesSequence(player)
        .mapNotNull { board -> board.minMax(depth - 1, player.enemy(), data)?.let { Node(board, it) } }
        .maxByOrNull(Node::eval)

internal fun Board.minMax(depth: Int, player: MaximizingPlayer, data: MinMaxData): Int? =
    if (depth == 0) {
        evaluation(player)
    } else {
        var minMaxData = data
        getAllMovesSequence(player)
            .mapNotNull { board -> board.minMax(depth - 1, player.enemy(), minMaxData) }
            .completableFold(initial = null) { old, new ->
                player
                    .bestEval(old ?: new, new)
                    .let { best ->
                        minMaxData = player.updateMinMaxData(best, minMaxData)
                        best to isNeedStop(minMaxData)
                    }
            }
    }

private fun Board.getAllMovesSequence(player: MaximizingPlayer): Sequence<Board> =
    filterIsInstance<Cell.Piece>()
        .asSequence()
        .filter(player::isSelfPiece)
        .map { startCell ->
            when (startCell) {
                is Cell.Piece.Man -> getAvailableCellsSequence(startCell)
                is Cell.Piece.King -> getAvailableCellsSequence(startCell)
            }
        }
        .flatten()

internal fun Board.evaluation(player: MaximizingPlayer): Int {
    val pieceList = filterIsInstance<Cell.Piece>()
    val manCells = pieceList.filterIsInstance<Cell.Piece.Man>()
    val kingCells = pieceList.filterIsInstance<Cell.Piece.King>()

    val playerMans = manCells.count(player::isSelfPiece)
    val playerKings = kingCells.count(player::isSelfPiece)
    val enemyMans = manCells.count(player::isEnemyPiece)
    val enemyKings = kingCells.count(player::isEnemyPiece)

    return when (0) {
        playerMans + playerKings -> Int.MIN_VALUE
        enemyMans + enemyKings -> Int.MAX_VALUE
        else -> 2 * (playerMans - enemyMans) + 5 * (playerKings - enemyKings)
    }
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