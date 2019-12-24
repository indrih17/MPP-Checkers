package com.kubsu.checkers.move

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.defaultBoard
import com.kubsu.checkers.functions.move.ai.getAvailableCellsSequence
import kotlin.test.Test
import kotlin.test.assertEquals

class KingMoveTests {
    private val defaultBoard = defaultBoard(8)
    private val color = CellColor.Light

    @Test fun aiMoveKingTest() {
        val king = Cell.Piece.King(7, 2, color)
        val board = defaultBoard
            .update(king)
            .update(Cell.Piece.Man(6, 3, color.enemy()))
        assertEquals(
            listOf(
                defaultBoard
                    .update(Cell.Piece.King(6, 1, color))
                    .update(Cell.Piece.Man(6, 3, color.enemy())),
                defaultBoard
                    .update(Cell.Piece.King(5, 0, color))
                    .update(Cell.Piece.Man(6, 3, color.enemy())),
                defaultBoard.update(Cell.Piece.King(5, 4, color)),
                defaultBoard.update(Cell.Piece.King(4, 5, color)),
                defaultBoard.update(Cell.Piece.King(3, 6, color)),
                defaultBoard.update(Cell.Piece.King(2, 7, color))
            ),
            board.getAvailableCellsSequence(king).toList()
        )
    }

    /*@Test fun availableCellsSimple1() {
        val king = Cell.Piece.King(7, 4, CellColor.Dark)
        val board = createBoard(8)
            .map { if (it is Cell.Piece) it.toEmpty() else it }
            .update(king)

        val leftDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(6, 3)),
            king.updateCoordinates(Cell.Empty(5, 2)),
            king.updateCoordinates(Cell.Empty(4, 1)),
            king.updateCoordinates(Cell.Empty(3, 0))
        )
        val rightDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(6, 5)),
            king.updateCoordinates(Cell.Empty(5, 6)),
            king.updateCoordinates(Cell.Empty(4, 7))
        )
        assertEquals(
            leftDiagonal + rightDiagonal,
            board.getAvailableCellsSequence(king).toList().map { it.cell }
        )
    }

    @Test fun availableCellsSimple2() {
        val king = Cell.Piece.King(4, 3, CellColor.Dark)
        val board = createBoard(8)
            .map { if (it is Cell.Piece) it.toEmpty() else it }
            .update(king)

        val upLeftDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(3, 2)),
            king.updateCoordinates(Cell.Empty(2, 1)),
            king.updateCoordinates(Cell.Empty(1, 0))
        )
        val upRightDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(3, 4)),
            king.updateCoordinates(Cell.Empty(2, 5)),
            king.updateCoordinates(Cell.Empty(1, 6)),
            king.updateCoordinates(Cell.Empty(0, 7))
        )
        val bottomLeftDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(5, 2)),
            king.updateCoordinates(Cell.Empty(6, 1)),
            king.updateCoordinates(Cell.Empty(7, 0))
        )
        val bottomRightDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(5, 4)),
            king.updateCoordinates(Cell.Empty(6, 5)),
            king.updateCoordinates(Cell.Empty(7, 6))
        )
        assertEquals(
            upLeftDiagonal + bottomLeftDiagonal + upRightDiagonal + bottomRightDiagonal,
            board.getAvailableCellsSequence(king).toList().map { it.cell }
        )
    }

    @Test fun availableCellsHard1() {
        val king = Cell.Piece.King(7, 4, CellColor.Dark)
        val board = createBoard(8)
            .map { if (it is Cell.Piece) it.toEmpty() else it }
            .update(king)
            .update(Cell.Piece.Man(6, 3, CellColor.Light))

        val leftDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(5, 2)),
            king.updateCoordinates(Cell.Empty(4, 1)),
            king.updateCoordinates(Cell.Empty(3, 0))
        )
        val rightDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(6, 5)),
            king.updateCoordinates(Cell.Empty(5, 6)),
            king.updateCoordinates(Cell.Empty(4, 7))
        )
        assertEquals(
            leftDiagonal + rightDiagonal,
            board.getAvailableCellsSequence(king).toList().map { it.cell }
        )
    }

    @Test fun availableCellsHard2() {
        val king = Cell.Piece.King(7, 4, CellColor.Dark)
        val board = createBoard(8)
            .map { if (it is Cell.Piece) it.toEmpty() else it }
            .update(king)
            .update(Cell.Piece.Man(6, 3, CellColor.Light))
            .update(Cell.Piece.Man(5, 2, CellColor.Light))

        val rightDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(6, 5)),
            king.updateCoordinates(Cell.Empty(5, 6)),
            king.updateCoordinates(Cell.Empty(4, 7))
        )
        assertEquals(
            rightDiagonal,
            board.getAvailableCellsSequence(king).toList().map { it.cell }
        )
    }

    @Test fun availableCellsHard3() {
        val king = Cell.Piece.King(7, 4, CellColor.Dark)
        val board = createBoard(8)
            .map { if (it is Cell.Piece) it.toEmpty() else it }
            .update(king)
            .update(Cell.Piece.Man(6, 3, CellColor.Light))
            .update(Cell.Piece.Man(4, 1, CellColor.Light))

        val leftDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(5, 2))
        )
        val rightDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(6, 5)),
            king.updateCoordinates(Cell.Empty(5, 6)),
            king.updateCoordinates(Cell.Empty(4, 7))
        )
        assertEquals(
            leftDiagonal + rightDiagonal,
            board.getAvailableCellsSequence(king).toList().map { it.cell }
        )
    }

    @Test fun availableCellsHard4() {
        val king = Cell.Piece.King(7, 4, CellColor.Dark)
        val board = createBoard(8)
            .map { if (it is Cell.Piece) it.toEmpty() else it }
            .update(king)
            .update(Cell.Piece.Man(4, 1, CellColor.Dark))

        val leftDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(6, 3)),
            king.updateCoordinates(Cell.Empty(5, 2))
        )
        val rightDiagonal = listOf(
            king.updateCoordinates(Cell.Empty(6, 5)),
            king.updateCoordinates(Cell.Empty(5, 6)),
            king.updateCoordinates(Cell.Empty(4, 7))
        )
        assertEquals(
            leftDiagonal + rightDiagonal,
            board.getAvailableCellsSequence(king).toList().map { it.cell }
        )
    }*/
}