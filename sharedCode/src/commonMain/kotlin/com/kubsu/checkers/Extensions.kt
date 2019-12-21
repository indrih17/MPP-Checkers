package com.kubsu.checkers

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlin.jvm.JvmName

inline fun <reified T> persistentList(size: Int, getValue: (Int) -> T): PersistentList<T> =
    List(size, getValue).toPersistentList()

inline fun <T : Any> Sequence<T>.completableNullableFold(
    initial: T?,
    operation: (acc: T?, T, completeFold: () -> Unit) -> T
): T? {
    var accumulator = initial
    var stop = false
    for (element in this) {
        accumulator = operation(accumulator, element) { stop = true }
        if (stop) return accumulator
    }
    return accumulator
}

@JvmName("completableFoldNullable")
inline fun <T : Any> Sequence<T>.completableFold(
    initial: T,
    operation: (acc: T, T, completeFold: () -> Unit) -> T
): T {
    var accumulator = initial
    var stop = false
    for (element in this) {
        accumulator = operation(accumulator, element) { stop = true }
        if (stop) return accumulator
    }
    return accumulator
}