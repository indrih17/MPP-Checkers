package com.kubsu.checkers

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.abs

inline fun <reified T> persistentList(size: Int, getValue: (Int) -> T): PersistentList<T> =
    List(size, getValue).toPersistentList()

inline fun <T : Any> Sequence<T>.completableFold(
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

@Suppress("NOTHING_TO_INLINE")
inline fun difference(a: Int, b: Int): Int = abs(a - b)