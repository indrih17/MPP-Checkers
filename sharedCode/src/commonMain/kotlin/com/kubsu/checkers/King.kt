package com.kubsu.checkers

import com.kubsu.checkers.data.*

fun Board.needToMadeKing(cell: Cell): Boolean =
    when (cell.type) {
        is CellType.Piece.White.Man ->
            cell.row == lastIndex

        is CellType.Piece.Black.Man ->
            cell.row == firstIndex

        else -> false
    }

fun Board.setKing(cell: Cell): Board =
    when (cell.type) {
        is CellType.Piece.White.Man ->
            update(cell.copy(type = CellType.Piece.White.King))

        is CellType.Piece.Black.Man ->
            update(cell.copy(type = CellType.Piece.Black.King))

        else ->
            throw IllegalArgumentException("Try to set king on cell: $cell")
    }