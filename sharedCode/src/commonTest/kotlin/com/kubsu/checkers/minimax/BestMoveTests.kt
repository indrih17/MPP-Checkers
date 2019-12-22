package com.kubsu.checkers.minimax

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.minmax.BestMove
import com.kubsu.checkers.data.minmax.MaximizingPlayer
import com.kubsu.checkers.data.minmax.MinMaxData
import com.kubsu.checkers.data.minmax.enemy
import com.kubsu.checkers.functions.ai.defaultEval
import com.kubsu.checkers.functions.ai.update
import kotlin.test.Test
import kotlin.test.assertEquals

class BestMoveTests {
    @Test fun updateTest1() {
        val color = CellColor.Light
        val man72 = Cell.Piece.Man(7, 2, color)
        val player = MaximizingPlayer.Self(color)
        val minMaxData = MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE)

        val enemy = player.enemy()
        val startBestMove = BestMove(man72, man72, player, minMaxData, player.defaultEval)
        val cell61 = Cell.Empty(6, 1)
        val newBestMove = BestMove(cell61, cell61, enemy, minMaxData, enemy.defaultEval)
        assertEquals(
            BestMove(
                man72,
                cell61,
                player,
                MinMaxData(alpha = Int.MAX_VALUE, beta = Int.MAX_VALUE),
                enemy.defaultEval
            ),
            startBestMove.update(newBestMove)
        )
    }

    @Test fun updateTest2() {
        val color = CellColor.Light
        val man72 = Cell.Piece.Man(7, 2, color)
        val cell61 = Cell.Empty(6, 1)
        val player = MaximizingPlayer.Self(color)
        val minMaxData = MinMaxData(alpha = Int.MIN_VALUE, beta = Int.MAX_VALUE)

        val cell50 = Cell.Empty(5, 0)
        val startBestMove = BestMove(man72, cell61, player, minMaxData, player.defaultEval)
        val newBestMove = BestMove(cell61, cell50, player, minMaxData, player.defaultEval)
        assertEquals(
            BestMove(man72, cell61, player, minMaxData, player.defaultEval),
            startBestMove.update(newBestMove)
        )
    }
}