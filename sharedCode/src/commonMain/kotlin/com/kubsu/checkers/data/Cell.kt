package com.kubsu.checkers.data

typealias Row = Int
typealias Column = Int

data class Cell(
    val row: Row,
    val column: Column,
    val type: CellType
)