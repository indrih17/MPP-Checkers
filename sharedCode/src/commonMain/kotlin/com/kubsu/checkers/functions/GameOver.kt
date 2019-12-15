package com.kubsu.checkers.functions

import com.kubsu.checkers.data.CellColor
import com.kubsu.checkers.data.Score

fun Score.isGameOver(): Boolean =
    light == 12 || dark == 12

fun Score.getWinner(): CellColor =
    if (light == 12) CellColor.Light else CellColor.Dark