package com.kubsu.checkers.data

typealias Matrix<T> = Array<Array<T>>

operator fun <T> Matrix<T>.set(row: Row, column: Column, value: T) {
    this[row][column] = value
}

operator fun <T> Matrix<T>.get(row: Row, column: Column): T =
    this[row][column]

inline fun <reified T> Matrix<T>.copy() =
    matrix(this)

inline fun <reified T, reified R> Matrix<T>.update(block: (T) -> R): Matrix<R> =
    matrix(size) { row, column -> block(get(row, column)) }

inline fun <reified T> matrix(size: Int, defaultValue: T): Matrix<T> =
    Array(size) { Array(size) { defaultValue } }

inline fun <reified T> matrix(size: Int, getValue: (row: Row, column: Column) -> T): Matrix<T> =
    Array(size) { row -> Array(size) { column -> getValue(row, column) } }

inline fun <reified T> matrix(original: Matrix<T>): Matrix<T> =
    matrix(original.size) { row, column -> original[row, column] }