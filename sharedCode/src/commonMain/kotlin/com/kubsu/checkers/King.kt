package com.kubsu.checkers

import com.kubsu.checkers.data.*

fun Cell.needToMadeKing(board: Board): Boolean =
    when (type) {
        is CellType.Piece.White.Man ->
            row == board.lastIndex

        is CellType.Piece.Black.Man ->
            row == board.firstIndex

        else -> false
    }

fun Cell.setKing(board: Board): Board =
    when (type) {
        is CellType.Piece.White.Man ->
            board.update(copy(type = CellType.Piece.White.King))

        is CellType.Piece.Black.Man ->
            board.update(copy(type = CellType.Piece.Black.King))

        else ->
            throw IllegalArgumentException("Try to set king on cell: $this")
    }