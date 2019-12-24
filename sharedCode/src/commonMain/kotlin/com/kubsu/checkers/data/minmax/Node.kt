package com.kubsu.checkers.data.minmax

import com.kubsu.checkers.data.entities.Board

data class Node(
    val board: Board,
    val eval: Int
)