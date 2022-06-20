package com.kubsu.checkers.data

import com.kubsu.checkers.data.entities.Cell
import kotlinx.serialization.Serializable

@Serializable
sealed class Failure {
    @Serializable
    data class IncorrectMove(val start: Cell.Piece, val finish: Cell.Empty) : Failure()
}