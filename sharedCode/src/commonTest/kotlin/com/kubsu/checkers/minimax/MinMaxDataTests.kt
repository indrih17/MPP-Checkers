package com.kubsu.checkers.minimax

import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.minmax.MaximizingPlayer
import com.kubsu.checkers.data.minmax.MinMaxData
import com.kubsu.checkers.functions.ai.defaultEval
import com.kubsu.checkers.functions.ai.isNeedStop
import com.kubsu.checkers.functions.ai.updateMinMaxData
import com.kubsu.checkers.functions.ai.minMaxEval
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MinMaxDataTests {
    @Test fun minMaxEvalTest() {
        val eval1 = 10; val eval2 = -10
        assertEquals(
            eval1,
            MaximizingPlayer.Self(CellColor.Light).minMaxEval(eval1, eval2)
        )
        assertEquals(
            eval2,
            MaximizingPlayer.Enemy(CellColor.Light).minMaxEval(eval1, eval2)
        )
    }

    @Test fun updateMinMaxDataTest() {
        val eval = 10
        val minMaxData = MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE)
        assertEquals(
            MinMaxData(eval, Int.MAX_VALUE),
            MaximizingPlayer.Self(CellColor.Light).updateMinMaxData(eval, minMaxData)
        )
        assertEquals(
            MinMaxData(Int.MIN_VALUE, eval),
            MaximizingPlayer.Enemy(CellColor.Light).updateMinMaxData(eval, minMaxData)
        )
    }

    @Test fun isNeedStopTest() {
        assertFalse(isNeedStop(MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE)))
        assertFalse(isNeedStop(MinMaxData(alpha = 10, beta = 11)))
        assertTrue(isNeedStop(MinMaxData(alpha = 10, beta = 10)))
    }

    @Test fun defaultEvalTest() {
        assertEquals(Int.MIN_VALUE, MaximizingPlayer.Self(CellColor.Light).defaultEval)
        assertEquals(Int.MAX_VALUE, MaximizingPlayer.Enemy(CellColor.Light).defaultEval)
    }
}