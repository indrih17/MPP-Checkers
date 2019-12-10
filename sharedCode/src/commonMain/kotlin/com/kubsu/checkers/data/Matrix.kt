package com.kubsu.checkers.data

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

inline class Matrix<T>(val value: PersistentList<PersistentList<T>>)

val Matrix<*>.size: Int
    inline get() = value.size

val Matrix<*>.firstIndex: Int
    inline get() = 0

val Matrix<*>.lastIndex: Int
    inline get() = value.lastIndex

inline fun <reified T> Matrix<T>.set(row: Row, column: Column, newValue: T): Matrix<T> {
    val newColumn = value[row].set(column, newValue)
    return Matrix(value.set(row, newColumn))
}

operator fun <T> Matrix<T>.get(row: Row, column: Column): T =
    value[row][column]

inline fun <reified T, reified R> Matrix<T>.map(block: (T) -> R): Matrix<R> =
    Matrix(value.map { it.map(block).toPersistentList() }.toPersistentList())

inline fun <reified T> matrix(size: Int, defaultValue: T): Matrix<T> =
    Matrix(persistentList(size) { persistentList(size) { defaultValue } })

inline fun <reified T> matrix(size: Int, getValue: (row: Row, column: Column) -> T): Matrix<T> =
    Matrix(persistentList(size) { row -> persistentList(size) { column -> getValue(row, column) } })