package com.kubsu.checkers.data.entities

/** Current board state. */
typealias Board = Matrix<Cell?>

@Suppress("UNUSED")
val Board.piecesAmount: Int
    inline get() = 12

/** Updating a cell on the board. */
fun Board.update(cell: Cell): Board =
    set(cell.row, cell.column, cell)

fun Board.get(cell: Cell, increase: Increase): Cell? =
    get(cell.row + increase.rowIncrease, cell.column + increase.columnIncrease)

val Board.rows: IntRange
    get() = firstIndex..lastIndex

val Board.columns: IntRange
    get() = firstIndex..lastIndex