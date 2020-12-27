package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Board

/**
 * MinMax move tree node.
 * @param board [Board].
 * @param eval Eval of the current state.
 */
data class Node(
    val board: Board,
    val eval: Int
)
