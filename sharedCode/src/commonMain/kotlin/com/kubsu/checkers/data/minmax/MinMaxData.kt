package com.kubsu.checkers.data.minmax

data class MinMaxData(val depth: Int, val alpha: Int, val beta: Int)

fun MinMaxData.decrementDepth(): MinMaxData =
    copy(depth = depth - 1)