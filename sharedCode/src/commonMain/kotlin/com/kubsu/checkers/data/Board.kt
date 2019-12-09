package com.kubsu.checkers.data

typealias Board = Matrix<CellType>

fun Board.update(cell: Cell): Board =
    set(cell.row, cell.column, cell.type)

fun Board.getCell(row: Row, column: Column): Cell =
    Cell(row, column, get(row, column))