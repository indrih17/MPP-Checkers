package com.kubsu.checkers.data.entities

import com.kubsu.checkers.Immutable
import com.kubsu.checkers.Parcelable
import com.kubsu.checkers.Parcelize
import kotlinx.serialization.Serializable

/**
 * - Man Шашка по-английски.
 * - King Дамка.
 * - Piece Фигура.
 */
@Serializable
@Immutable
sealed class Cell : Parcelable {
    abstract val row: Row
    abstract val column: Column

    @Serializable
    @Immutable
    sealed class Piece : Cell() {
        abstract val color: CellColor

        @Serializable
        @Parcelize
        @Immutable
        data class Man(
            override val row: Row,
            override val column: Column,
            override val color: CellColor
        ) : Piece() {
            override fun toString(): String = if (color == CellColor.Light) "w" else "b"
        }

        @Serializable
        @Parcelize
        @Immutable
        data class King(
            override val row: Row,
            override val column: Column,
            override val color: CellColor
        ) : Piece() {
            override fun toString(): String = if (color == CellColor.Light) "W" else "B"
        }
    }

    @Serializable
    @Parcelize
    @Immutable
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
