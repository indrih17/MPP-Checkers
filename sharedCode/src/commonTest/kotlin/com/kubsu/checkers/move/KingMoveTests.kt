package com.kubsu.checkers.move

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.functions.createBoard
import com.kubsu.checkers.functions.move.ai.getAvailableCellsSequence
import kotlin.test.Test
import kotlin.test.assertEquals

class KingMoveTests {
    @Test fun availableCellsSimple1() {
        val king = Cell.Piece.King(7, 4, CellColor.Dark)
        val board = createBoard(8)
            .map { if (it is Cell.Piece) it.toEmpty() else it }
            .update(king)

        val leftDiagonal = listOf(
            Cell.Empty(6, 3),
            Cell.Empty(5, 2),
            Cell.Empty(4, 1),
            Cell.Empty(3, 0)
        )
        val rightDiagonal = listOf(
            Cell.Empty(6, 5),
            Cell.Empty(5, 6),
            Cell.Empty(4, 7)
        )
        assertEquals(
            leftDiagonal + rightDiagonal,
            board.getAvailableCellsSequence(king, king).toList().map { it.cell }
        )
    }

    @Test fun availableCellsSimple2() {
        val king = Cell.Piece.King(4, 3, CellColor.Dark)
        val board = createBoard(8)
            .map { if (it is Cell.Piece) it.toEmpty() else it }
            .update(king)

        val upLeftDiagonal = listOf(
            Cell.Empty(3, 2),
            Cell.Empty(2, 1),
            Cell.Empty(1, 0)
        )
        val upRightDiagonal = listOf(
            Cell.Empty(3, 4),
            Cell.Empty(2, 5),
            Cell.Empty(1, 6),
            Cell.Empty(0, 7)
        )
        val bottomLeftDiagonal = listOf(
            Cell.Empty(5, 2),
            Cell.Empty(6, 1),
            Cell.Empty(7, 0)
        )
        val bottomRightDiagonal = listOf(
            Cell.Empty(5, 4),
            Cell.Empty(6, 5),
            Cell.Empty(7, 6)
        )
        assertEquals(
            upLeftDiagonal + bottomLeftDiagonal + upRightDiagonal + bottomRightDiagonal,
            board.getAvailableCellsSequence(king, king).toList().map { it.cell }
        )
    }

    @Test fun availableCellsHard1() {
        val king = Cell.Piece.King(7, 4, CellColor.Dark)
        val board = createBoard(8)
            .map { if (it is Cell.Piece) it.toEmpty() else it }
            .update(king)
            .update(Cell.Piece.Man(6, 3, CellColor.Light))

        val leftDiagonal = listOf(
            Cell.Empty(5, 2),
            Cell.Empty(4, 1),
            Cell.Empty(3, 0)
        )
        val rightDiagonal = listOf(
            Cell.Empty(6, 5),
            Cell.Empty(5, 6),
            Cell.Empty(4, 7)
        )
        assertEquals(
            leftDiagonal + rightDiagonal,
            board.getAvailableCellsSequence(king, king).toList().map { it.cell }
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
            Cell.Empty(6, 5),
            Cell.Empty(5, 6),
            Cell.Empty(4, 7)
        )
        assertEquals(
            rightDiagonal,
            board.getAvailableCellsSequence(king, king).toList().map { it.cell }
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
            Cell.Empty(5, 2)
        )
        val rightDiagonal = listOf(
            Cell.Empty(6, 5),
            Cell.Empty(5, 6),
            Cell.Empty(4, 7)
        )
        assertEquals(
            leftDiagonal + rightDiagonal,
            board.getAvailableCellsSequence(king, king).toList().map { it.cell }
        )
    }

    @Test fun availableCellsHard4() {
        val king = Cell.Piece.King(7, 4, CellColor.Dark)
        val board = createBoard(8)
            .map { if (it is Cell.Piece) it.toEmpty() else it }
            .update(king)
            .update(Cell.Piece.Man(4, 1, CellColor.Dark))

        val leftDiagonal = listOf(
            Cell.Empty(6, 3),
            Cell.Empty(5, 2)
        )
        val rightDiagonal = listOf(
            Cell.Empty(6, 5),
            Cell.Empty(5, 6),
            Cell.Empty(4, 7)
        )
        assertEquals(
            leftDiagonal + rightDiagonal,
            board.getAvailableCellsSequence(king, king).toList().map { it.cell }
        )
    }
}