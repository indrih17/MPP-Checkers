package com.kubsu.checkers.data

import com.kubsu.checkers.Immutable
import com.kubsu.checkers.Parcelable
import com.kubsu.checkers.Parcelize
import com.kubsu.checkers.data.entities.Cell

@Immutable
sealed class Failure : Parcelable {

    @Parcelize
    @Immutable
    data class IncorrectMove(val start: Cell.Piece, val finish: Cell.Empty) : Failure()
}