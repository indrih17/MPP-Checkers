package com.kubsu.checkers.data.entities

import kotlinx.serialization.Serializable

/**
 * - Man Шашка по-английски.
 * - King Дамка.
 * - Piece Фигура.
 */
@Serializable
sealed class Cell {
    abstract val row: Row
    abstract val column: Column

    @Serializable
    sealed class Piece : Cell() {
        abstract val color: CellColor

        @Serializable
        data class Man(
            override val row: Row,
            override val column: Column,
            override val color: CellColor
        ) : Piece() {
            override fun toString(): String = if (color == CellColor.Light) "w" else "b"
        }

        @Serializable
        data class King(
            override val row: Row,
            override val column: Column,
            override val color: CellColor
        ) : Piece() {
            override fun toString(): String = if (color == CellColor.Light) "W" else "B"
        }
    }

    @Serializable
    data class Empty(override val row: Row, override val column: Column) : Cell() {
        override fun toString(): String = " "
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
