package com.kubsu.checkers

import com.kubsu.checkers.data.matrix
import com.kubsu.checkers.data.set
import kotlin.test.Test
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class Test {
    @UseExperimental(ExperimentalTime::class)
    @Test
    fun testable() {
        val time = measureTime {
            matrix(8) { row, column -> row + column }[1, 1] = 4
        }
        throw IllegalArgumentException("Время: $time")
    }
}