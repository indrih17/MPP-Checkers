package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Board

/**
 * Situation analysis values, used to optimize calculations.
 * @param alpha The player's best move eval.
 * @param beta Worst enemy move eval.
 */
data class MinMaxData(val alpha: Int, val beta: Int)

/**
 * MinMax move tree node.
 * @param board [Board].
 * @param eval Eval of the current state.
 */
data class Node(
    val board: Board,
    val eval: Int
)
