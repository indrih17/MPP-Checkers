package com.kubsu.checkers.move

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.functions.createBoard
import com.kubsu.checkers.functions.move.ai.getAvailableCellsSequence
import com.kubsu.checkers.functions.move.ai.isBackMove
import kotlin.test.Test
import kotlin.test.assertEquals

class ManMoveTests {
    @Test fun availableCellsSimple() {
        val board = createBoard(8)

        val current50 = Cell.Piece.Man(5, 0, CellColor.Light)
        val new41 = Cell.Empty(4, 1)
        val new43 = Cell.Empty(4, 3)
        assertEquals(
            listOf(current50.updateCoordinates(new41)),
            board.getAvailableCellsSequence(current50).toList().map { it.cell }
        )
        val current52 = Cell.Piece.Man(5, 2, CellColor.Light)
        assertEquals(
            listOf(current50.updateCoordinates(new41), current50.updateCoordinates(new43)),
            board.getAvailableCellsSequence(current52).toList().map { it.cell }
        )
        val current61 = Cell.Piece.Man(6, 1, CellColor.Light)
        assertEquals(
            emptyList(),
            board.getAvailableCellsSequence(current61).toList().map { it.cell }
        )
    }

    @Test fun availableCellsHard() {
        val defaultBoard = createBoard(8).map { if (it is Cell.Piece) it.toEmpty() else it }

        val current52 = Cell.Piece.Man(5, 2, CellColor.Light)
        val board1 = defaultBoard
            .update(current52)
            .update(Cell.Piece.Man(4, 1, CellColor.Dark))
        val new30 = Cell.Empty(3, 0)
        val new43 = Cell.Empty(4, 3)
        assertEquals(
            listOf(current52.updateCoordinates(new30), current52.updateCoordinates(new43)),
            board1.getAvailableCellsSequence(current52).toList().map { it.cell }
        )

        val current41 = Cell.Piece.Man(4, 1, CellColor.Light)
        val board2 = defaultBoard
            .update(current41)
            .update(Cell.Piece.Man(3, 0, CellColor.Dark))
        val new32 = Cell.Empty(3, 2)
        assertEquals(
            listOf(current41.updateCoordinates(new32)),
            board2.getAvailableCellsSequence(current41).toList().map { it.cell }
        )

        val current50 = Cell.Piece.Man(5, 0, CellColor.Light)
        val board3 = defaultBoard
            .update(current50)
            .update(Cell.Piece.Man(6, 1, CellColor.Dark))
        val new41 = Cell.Empty(4, 1)
        val new72 = Cell.Empty(7, 2)
        assertEquals(
            listOf(current50.updateCoordinates(new41), current50.updateCoordinates(new72)),
            board3.getAvailableCellsSequence(current50).toList().map { it.cell }
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