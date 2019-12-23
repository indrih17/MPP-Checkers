package com.kubsu.checkers.data.entities

typealias Row = Int
typealias Column = Int

/**
 * Man - шашка по-английски
 * King - дамка.
 * Piece - фигура.
 */
sealed class Cell(open val row: Row, open val column: Column) {
    sealed class Piece(row: Row, column: Column, open val color: CellColor) : Cell(row, column) {
        data class Man(
            override val row: Row,
            override val column: Column,
            override val color: CellColor
        ) : Piece(row, column, color)

        data class King(
            override val row: Row,
            override val column: Column,
            override val color: CellColor
        ) : Piece(row, column, color)
    }

    data class Empty(override val row: Row, override val column: Column) : Cell(row, column)
}

fun Cell.Piece.Man.toKing() =
    Cell.Piece.King(row, column, color)

fun Cell.Piece.toEmpty() =
    Cell.Empty(row, column)

infix fun Cell.Piece.isSelf(cell: Cell.Piece): Boolean =
    !isEnemy(cell)

infix fun Cell.Piece.isEnemy(cell: Cell.Piece): Boolean =
    color != cell.color

infix fun Cell.Piece.isEnemy(cellColor: CellColor): Boolean =
    color != cellColor

infix fun Cell.Piece.isSameColor(cell: Cell.Piece): Boolean =
    color == cell.color

inline fun <reified T : Cell> T.updateCoordinates(new: Cell): T =
    when (val cell: Cell = this) {
        is Cell.Piece.King -> cell.copy(new.row, new.column) as T
        is Cell.Piece.Man -> cell.copy(new.row, new.column) as T
        is Cell.Empty -> cell.copy(new.row, new.column) as T
    }

