package com.kubsu.checkers.data

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
    data class Inaccessible(override val row: Row, override val column: Column) : Cell(row, column)
}

sealed class CellColor {
    object White : CellColor()
    object Black : CellColor()
}

fun Cell.Piece.Man.toKing() =
    Cell.Piece.King(row, column, color)

fun Cell.Piece.toEmpty() =
    Cell.Empty(row, column)

infix fun Cell.Piece.isEnemy(cell: Cell.Piece): Boolean =
    color != cell.color

infix fun Cell.Piece.isSameColor(cell: Cell.Piece): Boolean =
    color == cell.color

fun Cell.Piece.colorOfEnemy(): CellColor =
    listOf(CellColor.White, CellColor.Black).minus(color).single()

fun Cell.updateCoordinates(new: Cell): Cell =
    when (this) {
        is Cell.Piece.King -> copy(new.row, new.column)
        is Cell.Piece.Man -> copy(new.row, new.column)
        is Cell.Empty -> copy(new.row, new.column)
        is Cell.Inaccessible -> copy(new.row, new.column)
    }

