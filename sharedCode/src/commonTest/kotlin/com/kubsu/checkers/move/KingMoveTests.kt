package com.kubsu.checkers.move

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.defaultBoard
import com.kubsu.checkers.functions.move.ai.getAvailableCellsSequence
import com.kubsu.checkers.functions.move.move
import kotlin.test.Test
import kotlin.test.assertEquals

class KingMoveTests {
    private val defaultBoard = defaultBoard()
    private val color = CellColor.Light
    private val king72 = Cell.Piece.King(7, 2, color)

    /** Простой переход с клетки (7,2) по диагонали: (6,3), (5, 4), (4, 5), (3, 6), (2, 7). */
    @Test
    fun simpleMoveTest() {
        val destination63 = Cell.Empty(row = 6, column = 3)
        assertEquals(
            expected = defaultBoard.update(king72.updateCoordinates(destination63)),
            actual = defaultBoard.update(king72).move(current = king72, empty = destination63).first
        )
        val destination54 = Cell.Empty(row = 5, column = 4)
        assertEquals(
            expected = defaultBoard.update(king72.updateCoordinates(destination54)),
            actual = defaultBoard.update(king72).move(current = king72, empty = destination54).first
        )
        val destination45 = Cell.Empty(row = 4, column = 5)
        assertEquals(
            expected = defaultBoard.update(king72.updateCoordinates(destination45)),
            actual = defaultBoard.update(king72).move(current = king72, empty = destination45).first
        )
        val destination36 = Cell.Empty(row = 3, column = 6)
        assertEquals(
            expected = defaultBoard.update(king72.updateCoordinates(destination36)),
            actual = defaultBoard.update(king72).move(current = king72, empty = destination36).first
        )
        val destination27 = Cell.Empty(row = 2, column = 7)
        assertEquals(
            expected = defaultBoard.update(king72.updateCoordinates(destination27)),
            actual = defaultBoard.update(king72).move(current = king72, empty = destination27).first
        )
    }

    @Test fun aiMoveKingTest() {
        val king = Cell.Piece.King(7, 2, color)
        val board = defaultBoard
            .update(king)
            .update(Cell.Piece.Man(6, 3, color.enemy))
        assertEquals(
            listOf(
                defaultBoard
                    .update(Cell.Piece.King(6, 1, color))
                    .update(Cell.Piece.Man(6, 3, color.enemy)),
                defaultBoard
                    .update(Cell.Piece.King(5, 0, color))
                    .update(Cell.Piece.Man(6, 3, color.enemy)),
                defaultBoard.update(Cell.Piece.King(5, 4, color)),
                defaultBoard.update(Cell.Piece.King(4, 5, color)),
                defaultBoard.update(Cell.Piece.King(3, 6, color)),
                defaultBoard.update(Cell.Piece.King(2, 7, color))
            ),
            board.getAvailableCellsSequence(king).toList()
        )
    }
}