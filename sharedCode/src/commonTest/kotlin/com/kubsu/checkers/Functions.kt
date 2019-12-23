package com.kubsu.checkers

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.map
import com.kubsu.checkers.data.entities.toEmpty
import com.kubsu.checkers.functions.createBoard

fun defaultBoard(size: Int) = createBoard(size).map { if (it is Cell.Piece) it.toEmpty() else it }