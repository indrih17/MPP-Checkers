package com.kubsu.checkers.data.entities

import com.kubsu.checkers.persistentList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.mutate

inline class Matrix<T>(val value: PersistentList<PersistentList<T>>) {
    override fun toString(): String =
        "\n${value.joinToString("\n") { list -> list.joinToString(separator = " ") }}\n"
}

@Suppress("UNUSED")
val Matrix<*>.firstIndex: Int
    inline get() = 0

val Matrix<*>.lastIndex: Int
    inline get() = value.lastIndex

inline fun <reified T> Matrix<T>.set(row: Row, column: Column, newValue: T): Matrix<T> {
    val newColumn = value[row].mutate { it[column] = newValue }
    return Matrix(value.mutate { it[row] = newColumn })
}

fun <T> Matrix<T>.get(row: Row, column: Column): T =
    value[row][column]

fun <T> Matrix<T>.getOrNull(row: Row, column: Column): T? =
    value.getOrNull(row)?.getOrNull(column)

inline fun <reified T> matrix(size: Int, defaultValue: T): Matrix<T> =
    Matrix(persistentList(size) { persistentList(size) { defaultValue } })

inline fun <reified T> matrix(size: Int, getValue: (row: Row, column: Column) -> T): Matrix<T> =
    Matrix(persistentList(size) { row -> persistentList(size) { column -> getValue(row, column) } })

inline fun <reified R> Matrix<*>.filterIsInstance(): ImmutableList<R> =
    value.map { it.filterIsInstance<R>() }.flatten().toImmutableList()