package com.kubsu.checkers.minimax

import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.defaultBoard
import com.kubsu.checkers.functions.ai.BestMove
import com.kubsu.checkers.functions.ai.getBestMoveOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

class MinMaxTests {
    @Test fun manVsMansTest1() {
        val defaultBoard = defaultBoard(8)
        val current = Cell.Piece.Man(7, 2, CellColor.Light)
        val enemy1 = Cell.Piece.Man(5, 0, CellColor.Dark)
        val enemy2 = Cell.Piece.Man(5, 4, CellColor.Dark)
        val gameState = GameState(
            board = defaultBoard.update(current).update(enemy1).update(enemy2),
            score = Score(0,0),
            activePlayerColor = CellColor.Light,
            movesWithoutAttacks = 0
        )
        assertEquals(
            BestMove(
                startCell = current,
                finishCell = Cell.Empty(6, 1),
                eval = -1
            ),
            gameState.getBestMoveOrNull()
        )
    }

    @Test fun manVsMansTest2() {
        val defaultBoard = defaultBoard(8)
        val current = Cell.Piece.Man(6, 3, CellColor.Light)
        val enemy1 = Cell.Piece.Man(4, 1, CellColor.Dark)
        val enemy2 = Cell.Piece.Man(3, 6, CellColor.Dark)
        val gameState = GameState(
            board = defaultBoard.update(current).update(enemy1).update(enemy2),
            score = Score(0,0),
            activePlayerColor = CellColor.Light,
            movesWithoutAttacks = 0
        )
        assertEquals(
            BestMove(
                startCell = current,
                finishCell = Cell.Empty(5, 4),
                eval = 1
            ),
            gameState.getBestMoveOrNull()
        )
    }

    @Test fun kingVsManTest1() {
        val defaultBoard = defaultBoard(8)
        val current = Cell.Piece.King(7, 2, CellColor.Light)
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
                eval = 9
            ),
            gameState.getBestMoveOrNull()
        )
    }
}