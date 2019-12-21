package com.kubsu.checkers.data

import com.kubsu.checkers.data.entities.Cell

sealed class Failure {
    data class IncorrectMove(val start: Cell.Piece, val finish: Cell.Empty) : Failure()
    object NoMoves : Failure()
}