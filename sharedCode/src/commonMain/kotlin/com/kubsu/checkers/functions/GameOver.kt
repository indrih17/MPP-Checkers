package com.kubsu.checkers.functions

import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.game.Score

fun Score.isGameOver(): Boolean =
    light == 12 || dark == 12

fun Score.getWinner(): CellColor =
    if (light == 12) CellColor.Light else CellColor.Dark