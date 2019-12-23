package com.kubsu.checkers.minimax

import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.minmax.MaximizingPlayer
import com.kubsu.checkers.data.minmax.MinMaxData
import com.kubsu.checkers.data.minmax.Node
import com.kubsu.checkers.functions.ai.create
import com.kubsu.checkers.functions.ai.update
import kotlin.test.Test
import kotlin.test.assertEquals

class NodeTests {
    @Test fun createForSelfTest() {
        val color = CellColor.Light
        val current = Cell.Piece.Man(7, 2, color)
        val node = Node(
            startCell = Cell.Piece.Man(6, 3, color),
            finishCell = null,
            player = MaximizingPlayer.Enemy(color),
            eval = 10
        )
        assertEquals(
            Node(
                startCell = current,
                finishCell = Cell.Empty(6, 3),
                player = MaximizingPlayer.Self(color),
                eval = 10
            ),
            node.create(current, MaximizingPlayer.Self(color))
        )
    }

    @Test fun updateForSelfTest() {
        val color = CellColor.Light
        val currentNode = Node(
            startCell = Cell.Piece.Man(7, 2, color),
            finishCell = Cell.Empty(6, 1),
            player = MaximizingPlayer.Self(color),
            eval = 10
        )
        val newNode = Node(
            startCell = Cell.Piece.Man(6, 3, color),
            finishCell = null,
            player = MaximizingPlayer.Enemy(color),
            eval = 15
        )
        assertEquals(
            Node(
                startCell = Cell.Piece.Man(7, 2, color),
                finishCell = Cell.Empty(6, 3),
                player = MaximizingPlayer.Self(color),
                eval = 15
            ),
            currentNode.update(newNode)
        )
    }
}