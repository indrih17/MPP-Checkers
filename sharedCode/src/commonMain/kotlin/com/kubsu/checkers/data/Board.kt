package com.kubsu.checkers.data

typealias Board = Matrix<Cell>

fun Board.update(cell: Cell): Board =
    set(cell.row, cell.column, cell)

fun Board.swap(first: Cell, second: Cell): Board =
    set(first.row, first.column, second).set(second.row, second.column, first)