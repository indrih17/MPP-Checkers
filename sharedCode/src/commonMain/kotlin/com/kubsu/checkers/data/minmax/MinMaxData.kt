package com.kubsu.checkers.data.minmax

/**
 * Situation analysis values, used to optimize calculations.
 * @param alpha The player's best move eval.
 * @param beta Worst enemy move eval.
 */
data class MinMaxData(val alpha: Int, val beta: Int)