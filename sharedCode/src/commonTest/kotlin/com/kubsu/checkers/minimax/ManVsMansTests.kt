package com.kubsu.checkers.minimax

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.entities.update
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.defaultBoard
import com.kubsu.checkers.functions.ai.BestMove
import com.kubsu.checkers.functions.ai.getBestMoveOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

class ManVsMansTests {
    @Test
    fun test1() {
        val defaultBoard = defaultBoard(8)
        val current = Cell.Piece.Man(7, 2, CellColor.Light)
        val enemy = Cell.Piece.Man(6, 1, CellColor.Dark)
        val gameState = GameState(
            board = defaultBoard.update(current).update(enemy),
            score = Score(0,0),
            activePlayerColor = CellColor.Light,
            movesWithoutAttacks = 0
        )
        assertEquals(
            BestMove(
                startCell = current,
                finishCell = Cell.Empty(5, 0),
                eval = 1
            ),
            gameState.getBestMoveOrNull()
        )
    }

    @Test
    fun test2() {
        val defaultBoard = defaultBoard(8)
        val current = Cell.Piece.Man(6, 3, CellColor.Light)
        val enemy = Cell.Piece.Man(5, 4, CellColor.Dark)
        val gameState = GameState(
            board = defaultBoard.update(current).update(enemy),
            score = Score(0,0),
            activePlayerColor = CellColor.Light,
            movesWithoutAttacks = 0
        )
        assertEquals(
            BestMove(
                startCell = current,
                finishCell = Cell.Empty(4, 5),
                eval = 1
            ),
            gameState.getBestMoveOrNull()
        )
    }

    @Test
    fun test3() {
        val defaultBoard = defaultBoard(8)
        val current = Cell.Piece.Man(7, 2, CellColor.Light)
        val enemy = Cell.Piece.Man(5, 4, CellColor.Dark)
        val gameState = GameState(
            board = defaultBoard.update(current).update(enemy),
            score = Score(0,0),
            activePlayerColor = CellColor.Light,
            movesWithoutAttacks = 0
        )
        assertEquals(
            BestMove(
                startCell = current,
                finishCell = Cell.Empty(6, 1),
                eval = 0
            ),
            gameState.getBestMoveOrNull()
        )
    }

}