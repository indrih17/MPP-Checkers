package com.kubsu.checkers.data

typealias Board = Matrix<CellType>

fun Board.update(cell: Cell): Board =
    matrix(original = this).apply { set(cell.row, cell.column, cell.type) }