package com.kubsu.checkers

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.minmax.MaximizingPlayer
import com.kubsu.checkers.functions.ai.minimax
import com.kubsu.checkers.functions.createBoard
import com.kubsu.checkers.functions.move.ai.getAvailableCellsSequence
import kotlin.test.Test
import kotlin.test.assertEquals

class LightTests {
    private val board = createBoard(8)
    private val color = CellColor.Light
    private val cell50 = Cell.Piece.Man(5, 0, color)
    private val cell41 = Cell.Empty(4, 1)
    private val cell30 = Cell.Empty(3, 0)
    private val cell32 = Cell.Empty(3, 2)
    private val cell70 = Cell.Piece.Man(7, 0, color)

    @Test fun minimax() {
        val minimax30 = board.minimax(cell50, cell30)
        assertEquals(-2, minimax30.eval)
        assertEquals(cell30, minimax30.startCell)
        assertEquals(cell30, minimax30.finishCell)
        assertEquals(MaximizingPlayer.Self(color), minimax30.player)

        val minimax41 = board.minimax(cell50, cell41)
        assertEquals(0, minimax41.eval)
        assertEquals(cell41, minimax41.startCell)
        assertEquals(cell30, minimax41.finishCell)
        assertEquals(MaximizingPlayer.Self(color), minimax41.player)

        val minimax50 = board.minimax(cell50, cell50)
        assertEquals(0, minimax50.eval)
        assertEquals(cell50, minimax50.startCell)
        assertEquals(cell41, minimax50.finishCell)
        assertEquals(MaximizingPlayer.Self(color), minimax50.player)

        val minimax70 = board.minimax(cell70, cell70)
        assertEquals(0, minimax70.eval)
        assertEquals(cell70, minimax70.startCell)
        assertEquals(cell70, minimax70.finishCell)
        assertEquals(MaximizingPlayer.Self(color), minimax70.player)
    }

    @Test fun getAvailableCells() {
        val result1 = board.getAvailableCellsSequence(cell50, cell50).single()
        assertEquals(cell41, result1)

        val result2 = board.getAvailableCellsSequence(cell50, cell41).toList()
        assertEquals(listOf(cell30, cell32), result2)
    }
}