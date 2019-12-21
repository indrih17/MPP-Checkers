package com.kubsu.checkers

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.functions.move.human.move
import com.kubsu.checkers.functions.move.human.needToMadeKing
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Test {
    @Test
    fun matrixSetOperator() {
        val original = matrix(8, 0)
        val new = original.set(1, 1, 100)
        val expected =
            matrix(8) { row, column -> if (row == 1 && column == 1) 100 else 0 }
        assertEquals(new, expected)
    }

    @Test
    fun setKing() {
        val board: Board = matrix(8, Cell::Empty)
        val currentMan = Cell.Piece.Man(1, 0, CellColor.Light)
        val destination = Cell.Empty(0, 1)
        val score = Score()
        board.move(currentMan, destination, score).fold(
            ifLeft = { throw IllegalStateException() },
            ifRight = { assertTrue(it.board.get(0, 1) is Cell.Piece.King) }
        )
    }

    @Test
    fun needToMakeKing() {
        val board: Board = matrix(8, Cell::Empty)
        val man = Cell.Piece.Man(0, 2, CellColor.Light)
        assertEquals(true, board.needToMadeKing(man))
    }
}