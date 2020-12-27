package com.kubsu.checkers.minimax

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.data.minmax.Node
import com.kubsu.checkers.defaultBoard
import com.kubsu.checkers.functions.ai.getBestMoveOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

class MinMaxTests {
    private val defaultBoard = defaultBoard()
    private val color = CellColor.Light

    @Test fun manVsMansTest1() {
        val gameState = GameState(
            board = defaultBoard
                .update(Cell.Piece.Man(7, 2, color))
                .update(Cell.Piece.Man(5, 0, color.enemy()))
                .update(Cell.Piece.Man(5, 4, color.enemy())),
            score = Score(0, 0),
            activePlayer = CellColor.Light,
            simpleMoves = 0
        )
        assertEquals(
            Node(
                board = defaultBoard
                    .update(Cell.Piece.Man(6, 1, color))
                    .update(Cell.Piece.Man(5, 0, color.enemy()))
                    .update(Cell.Piece.Man(5, 4, color.enemy())),
                eval = -2147483648
            ),
            gameState.getBestMoveOrNull()
        )
    }

    @Test fun manVsMansTest2() {
        val gameState = GameState(
            board = defaultBoard
                .update(Cell.Piece.Man(6, 3, color))
                .update(Cell.Piece.Man(4, 1, color.enemy()))
                .update(Cell.Piece.Man(3, 6, color.enemy())),
            score = Score(0, 0),
            activePlayer = CellColor.Light,
            simpleMoves = 0
        )
        assertEquals(
            Node(
                board = defaultBoard
                    .update(Cell.Piece.Man(5, 2, color))
                    .update(Cell.Piece.Man(4, 1, color.enemy()))
                    .update(Cell.Piece.Man(3, 6, color.enemy())),
                eval = Int.MIN_VALUE
            ),
            gameState.getBestMoveOrNull()
        )
    }

    @Test fun kingVsManTest1() {
        val gameState = GameState(
            board = defaultBoard
                .update(Cell.Piece.King(7, 2, color))
                .update(Cell.Piece.Man(5, 4, color.enemy())),
            score = Score(0, 0),
            activePlayer = CellColor.Light,
            simpleMoves = 0
        )
        assertEquals(
            Node(
                defaultBoard.update(Cell.Piece.King(4, 5, color)),
                eval = Int.MAX_VALUE
            ),
            gameState.getBestMoveOrNull()
        )
    }
}