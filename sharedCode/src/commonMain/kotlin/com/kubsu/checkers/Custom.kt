package com.kubsu.checkers

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.abs

inline fun <reified T> persistentList(size: Int, getValue: (Int) -> T): PersistentList<T> =
    List(size, getValue).toPersistentList()

inline fun <T : Any> Sequence<T>.completableFold(
    initial: T?,
    operation: (old: T?, new: T, complete: () -> Unit) -> T
): T? {
    var old = initial
    var stop = false
    for (element in this) {
        old = operation(old, element) { stop = true }
        if (stop) return old
    }
    return old
}

@Suppress("NOTHING_TO_INLINE")
inline fun difference(a: Int, b: Int): Int = abs(a - b)