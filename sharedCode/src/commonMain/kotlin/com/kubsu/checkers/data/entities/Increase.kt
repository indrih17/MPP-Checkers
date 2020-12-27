package com.kubsu.checkers.data.entities

/**
 * Possible moves around for a piece.
 * @param rowIncrease The direction for the row.
 * @param columnIncrease The direction for the column.
 */
data class Increase(val rowIncrease: Int, val columnIncrease: Int)

/** 4 possible moves around. Implemented using [Sequence] for optimization so as not to calculate unnecessary moves. */
internal val increasesSequence: Sequence<Increase> = sequenceOf(
    Increase(rowIncrease = -1, columnIncrease = -1),
    Increase(rowIncrease = +1, columnIncrease = -1),
    Increase(rowIncrease = -1, columnIncrease = +1),
    Increase(rowIncrease = +1, columnIncrease = +1)
)
