package com.kubsu.checkers

import com.kubsu.checkers.data.*

fun Board.needToMadeKing(cell: Cell.Piece.Man): Boolean =
    cell.row == if (cell.color is Color.White) firstIndex else lastIndex

fun Board.setKing(cell: Cell.Piece.Man): Board =
    update(cell.toKing())