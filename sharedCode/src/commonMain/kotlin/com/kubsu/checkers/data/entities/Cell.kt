package com.kubsu.checkers.data.entities

/**
 * - Man Шашка по-английски.
 * - King Дамка.
 * - Piece Фигура.
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
        is Piece.Man -> if (color == CellColor.Light) "w" else "b"
        is Piece.King -> if (color == CellColor.Light) "W" else "B"
        is Empty -> " "
    }
}

fun Cell.Piece.toEmpty() =
    Cell.Empty(row, column)

fun king(man: Cell.Piece.Man) =
    Cell.Piece.King(man.row, man.column, man.color)

enum class CellColor {
    Light { override val enemy: CellColor get() = Dark },
    Dark { override val enemy: CellColor get() = Light };

    abstract val enemy: CellColor
}

infix fun Cell.Piece.isEnemy(cell: Cell.Piece): Boolean =
    color.enemy == cell.color

@Suppress("UNCHECKED_CAST")
infix fun < T : Cell> T.updateCoordinates(new: Cell): T =
    when (val cell: Cell = this) {
        is Cell.Piece.King -> cell.copy(new.row, new.column) as T
        is Cell.Piece.Man -> cell.copy(new.row, new.column) as T
        is Cell.Empty -> cell.copy(new.row, new.column) as T
    }
