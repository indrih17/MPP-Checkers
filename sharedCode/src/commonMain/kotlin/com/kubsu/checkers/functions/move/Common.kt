package com.kubsu.checkers.functions.move

import com.kubsu.checkers.data.entities.*

internal fun Board.kill(current: Cell.Piece.King, enemy: Cell.Piece, empty: Cell.Empty): Pair<Board, Cell.Piece.King> =
    killT(enemy).move(current = current, empty = empty)

internal fun Board.kill(current: Cell.Piece.Man, enemy: Cell.Piece, empty: Cell.Empty): Pair<Board, Cell.Piece> =
    killT(enemy).move(current = current, empty = empty)

/** @return move with updated board and piece. */
internal fun Board.move(current: Cell.Piece.King, empty: Cell.Empty): Pair<Board, Cell.Piece.King> =
    moveT(current, empty)

/** @return move with updated board and piece. */
@Suppress("NAME_SHADOWING")
internal fun Board.move(current: Cell.Piece.Man, empty: Cell.Empty): Pair<Board, Cell.Piece> {
    val (board, current) = moveT(current, empty)
    return if (board.needToMadeKing(current)) {
        val king = king(current)
        board.update(king) to king
    } else {
        board to current
    }
}

private fun Board.killT(enemy: Cell.Piece): Board =
    update(enemy.toEmpty())

private fun <T : Cell.Piece> Board.moveT(current: T, empty: Cell.Empty): Pair<Board, T> {
    val currentAfterMove: T = current updateCoordinates empty
    val board = update(currentAfterMove).update(current.toEmpty())
    return board to currentAfterMove
}

/** @return true if a [man] is to be made king. */
fun Board.needToMadeKing(man: Cell.Piece.Man): Boolean =
    man.row == when (man.color) {
        CellColor.Light -> firstIndex
        CellColor.Dark -> lastIndex
    }
