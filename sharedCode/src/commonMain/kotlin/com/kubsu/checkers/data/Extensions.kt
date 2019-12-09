package com.kubsu.checkers.data

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

inline fun <reified T> persistentList(size: Int, getValue: (Int) -> T): PersistentList<T> =
    List(size, getValue).toPersistentList()