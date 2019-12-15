package com.kubsu.checkers

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

inline fun <reified T> persistentList(size: Int, getValue: (Int) -> T): PersistentList<T> =
    List(size, getValue).toPersistentList()

inline fun <T : Any> Sequence<T>.foldT(initial: T?, operation: (acc: T?, T) -> T): T? {
    var accumulator = initial
    for (element in this) accumulator = operation(accumulator, element)
    return accumulator
}
