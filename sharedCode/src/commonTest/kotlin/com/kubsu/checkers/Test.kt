package com.kubsu.checkers

import com.kubsu.checkers.data.Board
import com.kubsu.checkers.data.Cell
import com.kubsu.checkers.data.matrix
import com.kubsu.checkers.data.set
import com.kubsu.checkers.functions.move.getIntermediateCells
import kotlin.test.Test
import kotlin.test.assertEquals

class Test {
    @Test
    fun matrixSetOperator() {
        val original = matrix(8, 0)
        val new = original.set(1, 1, 100)
        val expected = matrix(8) { row, column -> if (row == 1 && column == 1) 100 else 0 }
        assertEquals(new, expected)
    }
}