package com.kubsu.checkers.data.entities

sealed class CellColor {
    object Light : CellColor()
    object Dark : CellColor()
}

/** @return the enemy's color for [this]. */
fun CellColor.enemy(): CellColor =
    listOf(CellColor.Light, CellColor.Dark).minus(this).single()