package com.kubsu.checkers

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.abs

inline fun <reified T> persistentList(size: Int, getValue: (Int) -> T): PersistentList<T> =
    List(size, getValue).toPersistentList()

inline fun <T : Any> Sequence<T>.completableFold(initial: T?, operation: (old: T?, new: T) -> Pair<T, Boolean>): T? {
    var latest = initial
    for (element in this) {
        operation(latest, element).let { (updated, complete) ->
            if (complete) return updated else latest = updated
        }
    }
    return latest
}

@Suppress("NOTHING_TO_INLINE")
inline fun difference(a: Int, b: Int): Int = abs(a - b)