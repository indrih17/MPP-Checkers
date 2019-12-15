package com.kubsu.checkers.data.entities

typealias Board = Matrix<Cell?>

fun Board.update(cell: Cell): Board =
    set(cell.row, cell.column, cell)

fun Board.swap(first: Cell.Piece, second: Cell.Empty): Board =
    update(first.updateCoordinates(second))
        .update(second.updateCoordinates(first))

val Board.rows: IntRange
    get() = firstIndex..lastIndex

val Board.columns: IntRange
    get() = firstIndex..lastIndex