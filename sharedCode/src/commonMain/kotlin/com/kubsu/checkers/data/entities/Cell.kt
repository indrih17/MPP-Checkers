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

    override fun toString(): String = when (this) {
        is Piece.Man -> if (color is CellColor.Light) "w" else "b"
        is Piece.King -> if (color is CellColor.Light) "W" else "B"
        is Empty -> " "
    }
}

fun king(man: Cell.Piece.Man) =
    Cell.Piece.King(man.row, man.column, man.color)

fun Cell.Piece.toEmpty() =
    Cell.Empty(row, column)

fun empty(piece: Cell.Piece) =
    Cell.Empty(piece.row, piece.column)

infix fun Cell.Piece.isSelf(cell: Cell.Piece): Boolean =
    !isEnemy(cell)

infix fun Cell.Piece.isEnemy(cell: Cell.Piece): Boolean =
    color != cell.color

infix fun Cell.Piece.isEnemy(cellColor: CellColor): Boolean =
    color != cellColor

infix fun Cell.Piece.isSameColor(cell: Cell.Piece): Boolean =
    color == cell.color

inline infix fun <reified T : Cell> T.updateCoordinates(new: Cell): T =
    when (val cell: Cell = this) {
        is Cell.Piece.King -> cell.copy(new.row, new.column) as T
        is Cell.Piece.Man -> cell.copy(new.row, new.column) as T
        is Cell.Empty -> cell.copy(new.row, new.column) as T
    }

