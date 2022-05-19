package com.kubsu.checkers.move

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.defaultBoard
import com.kubsu.checkers.functions.move.ai.isBackMove
import com.kubsu.checkers.functions.move.ai.getAvailableCellsSequence
import com.kubsu.checkers.functions.move.move
import kotlin.test.Test
import kotlin.test.assertEquals

class ManMoveTests {
    private val defaultBoard = defaultBoard()
    private val color = CellColor.Light
    private val man72 = Cell.Piece.Man(7, 2, color)

    /** Простой переход с клетки (7,2) на (6,3). */
    @Test
    fun simpleMoveTest() {
        val destination63 = Cell.Empty(row = 6, column = 3)
        assertEquals(
            expected = defaultBoard.update(man72.updateCoordinates(destination63)),
            actual = defaultBoard.update(man72).move(current = man72, empty = destination63).first
        )
    }

    @Test fun aiMoveManTest() {
        val board = defaultBoard
            .update(man72)
            .update(Cell.Piece.Man(6, 3, color.enemy))
        assertEquals(
            listOf(
                defaultBoard
                    .update(Cell.Piece.Man(6, 1, color))
                    .update(Cell.Piece.Man(6, 3, color.enemy)),
                defaultBoard.update(Cell.Piece.Man(5, 4, color))
            ),
            board.getAvailableCellsSequence(man72).toList()
        )
    }

     @Test fun isBackMove() {
         assertEquals(
             listOf(false, true, false, true),
             increasesSequence.map { isBackMove(CellColor.Light, it) }.toList()
         )
         assertEquals(
             listOf(true, false, true, false),
             increasesSequence.map { isBackMove(CellColor.Dark, it) }.toList()
         )
     }
}