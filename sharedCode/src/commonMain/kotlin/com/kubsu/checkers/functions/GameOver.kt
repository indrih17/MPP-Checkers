package com.kubsu.checkers.functions

import com.kubsu.checkers.data.CellColor
import com.kubsu.checkers.data.Score

fun Score.isGameOver(): Boolean =
    white == 12 || black == 12

fun Score.getWinner(): CellColor =
    if (white == 12) CellColor.White else CellColor.Black