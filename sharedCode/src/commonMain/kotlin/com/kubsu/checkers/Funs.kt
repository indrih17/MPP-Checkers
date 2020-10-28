package com.kubsu.checkers

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

suspend inline fun <T> def(noinline block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Default, block = block)

@Suppress("NOTHING_TO_INLINE")
inline fun difference(a: Int, b: Int): Int = abs(a - b)