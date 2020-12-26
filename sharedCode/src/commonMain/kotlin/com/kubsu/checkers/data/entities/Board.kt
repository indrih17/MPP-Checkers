package com.kubsu.checkers.data.entities

typealias Board = Matrix<Cell?>

@Suppress("UNUSED")
val Board.piecesAmount: Int
    inline get() = 12

/** Updating a cell on the board. */
fun Board.update(cell: Cell): Board =
    set(cell.row, cell.column, cell)

fun Board.swap(first: Cell.Piece, second: Cell.Empty): Board =
    update(first updateCoordinates second)
        .update(second updateCoordinates first)

fun Board.get(cell: Cell, increase: Increase): Cell? =
    get(cell.row + increase.rowIncrease, cell.column + increase.columnIncrease)

fun Board.getOrNull(cell: Cell, increase: Increase): Cell? =
    getOrNull(cell.row + increase.rowIncrease, cell.column + increase.columnIncrease)

val Board.rows: IntRange
    get() = firstIndex..lastIndex

val Board.columns: IntRange
    get() = firstIndex..lastIndex