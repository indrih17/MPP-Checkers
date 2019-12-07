package com.kubsu.checkers.data

/**
 * Man - шашка по-английски
 * King - дамка.
 * Piece - фигура.
 */
sealed class CellType {
    sealed class Piece : CellType() {
        sealed class White : Piece() {
            object Man : White()
            object King : White()
        }

        sealed class Black : Piece() {
            object Man : Black()
            object King : Black()
        }
    }

    object Empty : CellType()
    object Inaccessible : CellType()
}