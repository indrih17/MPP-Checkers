package com.kubsu.checkers

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.minmax.MaximizingPlayer
import com.kubsu.checkers.functions.ai.minimax
import com.kubsu.checkers.functions.createBoard
import com.kubsu.checkers.functions.move.ai.getAvailableCellsSequence
import kotlin.test.Test
import kotlin.test.assertEquals

class DarkTests {
    private val board = createBoard(8)
    private val color = CellColor.Dark
    private val cell01 = Cell.Piece.Man(0, 1, color)

    @Test
    fun minimax() {
        val minimax01 = board.minimax(cell01, cell01)
        assertEquals(0, minimax01.eval)
        assertEquals(cell01, minimax01.startCell)
        assertEquals(cell01, minimax01.finishCell)
        assertEquals(MaximizingPlayer.Self(color), minimax01.player)
    }

    @Test
    fun getAvailableCells() {
        val result1 = board.getAvailableCellsSequence(cell01, cell01).toList()
        assertEquals(emptyList(), result1)
    }
}