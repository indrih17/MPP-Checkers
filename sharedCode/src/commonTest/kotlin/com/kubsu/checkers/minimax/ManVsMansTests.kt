package com.kubsu.checkers.minimax

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.entities.enemy
import com.kubsu.checkers.data.entities.update
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.data.minmax.Node
import com.kubsu.checkers.defaultBoard
import com.kubsu.checkers.functions.ai.getBestMoveOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

class ManVsMansTests {
    private val defaultBoard = defaultBoard()
    private val color = CellColor.Light

    @Test
    fun test1() {
        val gameState = GameState(
            board = defaultBoard
                .update(Cell.Piece.Man(7, 2, color))
                .update(Cell.Piece.Man(6, 1, color.enemy())),
            score = Score(0, 0),
            activePlayer = color,
            simpleMoves = 0
        )
        assertEquals(
            Node(
                board = defaultBoard.update(Cell.Piece.Man(5, 0, color)),
                eval = Int.MAX_VALUE
            ),
            gameState.getBestMoveOrNull()
        )
    }

    @Test
    fun test2() {
        val gameState = GameState(
            board = defaultBoard
                .update(Cell.Piece.Man(6, 3, color))
                .update(Cell.Piece.Man(5, 4, color.enemy())),
            score = Score(0, 0),
            activePlayer = color,
            simpleMoves = 0
        )
        assertEquals(
            Node(
                board = defaultBoard.update(Cell.Piece.Man(4, 5, color)),
                eval = Int.MAX_VALUE
            ),
            gameState.getBestMoveOrNull()
        )
    }

    @Test
    fun test3() {
        val gameState = GameState(
            board = defaultBoard
                .update(Cell.Piece.Man(7, 2, color))
                .update(Cell.Piece.Man(5, 4, color.enemy())),
            score = Score(0, 0),
            activePlayer = color,
            simpleMoves = 0
        )
        assertEquals(
            Node(
                board = defaultBoard
                    .update(Cell.Piece.Man(6, 1, color))
                    .update(Cell.Piece.Man(5, 4, color.enemy())),
                eval = Int.MIN_VALUE
            ),
            gameState.getBestMoveOrNull()
        )
    }

    @Test
    fun test4() {
        val gameState = GameState(
            board = defaultBoard
                .update(Cell.Piece.Man(5, 4, color))
                .update(Cell.Piece.Man(2, 7, color.enemy()))
                .update(Cell.Piece.Man(7, 6, color)),
            score = Score(0, 0),
            activePlayer = color,
            simpleMoves = 0
        )
        assertEquals(
            Node(
                board = defaultBoard
                    .update(Cell.Piece.Man(4, 5, color))
                    .update(Cell.Piece.Man(2, 7, color.enemy()))
                    .update(Cell.Piece.Man(7, 6, color)),
                eval = Int.MAX_VALUE
            ),
            gameState.getBestMoveOrNull()
        )
    }

}