package com.kubsu.checkers.data.entities

sealed class CellColor {
    object Light : CellColor()
    object Dark : CellColor()
}

fun CellColor.enemy(): CellColor =
    listOf(CellColor.Light, CellColor.Dark).minus(this).single()