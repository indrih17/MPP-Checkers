package com.kubsu.checkers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.get
import com.kubsu.checkers.functions.BOARD_SIZE
import kotlinx.collections.immutable.PersistentList
import com.kubsu.checkers.functions.createBoard

@Immutable
data class ImmutableList<T>(val list: List<T>): List<T> by list

typealias StateMatrix = ImmutableList<ImmutableList<MutableState<Cell>?>>

fun StateMatrix.update(board: Board) {
    forEachIndexed { row, cells ->
        cells.forEachIndexed { column, mutableState ->
            board.get(row, column)?.let {
                mutableState?.value = it
            }
        }
    }
}

@Composable
fun stateMatrix(): StateMatrix =
    createBoard(BOARD_SIZE)
        .value
        .map { cells: PersistentList<Cell?> ->
            ImmutableList(
                cells.map { cell -> cell?.let { mutableStateOf(it) } }
            )
        }
        .let(::ImmutableList)
