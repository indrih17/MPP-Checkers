package com.kubsu.checkers.data

typealias Row = Int
typealias Column = Int

/**
 * Man - шашка по-английски
 * King - дамка.
 * Piece - фигура.
 */
sealed class Cell(open val row: Row, open val column: Column) {
    sealed class Piece(row: Row, column: Column, open val color: Color) : Cell(row, column) {
        data class Man(
            override val row: Row,
            override val column: Column,
            override val color: Color
        ) : Piece(row, column, color)

        data class King(
            override val row: Row,
            override val column: Column,
            override val color: Color
        ) : Piece(row, column, color)
    }

    data class Empty(override val row: Row, override val column: Column) : Cell(row, column)
    data class Inaccessible(override val row: Row, override val column: Column) : Cell(row, column)
}

sealed class Color {
    object White : Color()
    object Black : Color()
}

fun Cell.Piece.Man.toKing() =
    Cell.Piece.King(row, column, color)

fun Cell.Piece.toEmpty() =
    Cell.Empty(row, column)

infix fun Cell.Piece.isEnemy(cell: Cell.Piece): Boolean =
    color != cell.color