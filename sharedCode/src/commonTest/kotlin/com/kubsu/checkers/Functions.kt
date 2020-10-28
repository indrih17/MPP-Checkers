package com.kubsu.checkers

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.Matrix
import com.kubsu.checkers.data.entities.toEmpty
import com.kubsu.checkers.functions.createBoard
import kotlinx.collections.immutable.toPersistentList

fun defaultBoard() = createBoard().map { if (it is Cell.Piece) it.toEmpty() else it }

inline fun <reified R, reified T> Matrix<T>.map(block: (T) -> R): Matrix<R> =
    Matrix(value.map { it.map(block).toPersistentList() }.toPersistentList())