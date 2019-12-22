package com.kubsu.checkers.data.entities

data class Increase(val rowIncrease: Int, val columnIncrease: Int)

internal val increasesSequence: Sequence<Increase> = sequenceOf(
    Increase(rowIncrease = -1, columnIncrease = -1),
    Increase(rowIncrease = +1, columnIncrease = -1),
    Increase(rowIncrease = -1, columnIncrease = +1),
    Increase(rowIncrease = +1, columnIncrease = +1)
)