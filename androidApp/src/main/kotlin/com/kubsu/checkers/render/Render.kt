package com.kubsu.checkers.render

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import com.kubsu.checkers.R
import com.kubsu.checkers.data.entities.*
import com.kubsu.checkers.data.game.UserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun TableLayout.clear() {
    removeAllViews()
    setBackgroundColor(Color.WHITE)
}

fun Context.tableRow() = TableRow(this).apply {
    isClickable = true
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

fun Context.cellImageView(cell: Cell?) = ImageView(this).apply {
    setBackgroundColor(Color.BLACK)
    setImageResource(
        when (cell) {
            is Cell.Piece.Man -> cell.res
            is Cell.Piece.King -> cell.res
            is Cell.Empty -> R.drawable.black_cell
            null -> R.drawable.white_cell
        }
    )
}

private val Cell.Piece.Man.res: Int
    get() = if (color is CellColor.Light) R.drawable.white_man else R.drawable.black_man

private val Cell.Piece.King.res: Int
    get() = if (color is CellColor.Light) R.drawable.white_king else R.drawable.black_king

fun UiState.render(tableLayout: TableLayout, onClick: (suspend (Cell) -> Unit)? = null) {
    val board = userState.gameState.board
    for (row in board.rows) {
        val tableRow = tableLayout.context.tableRow()

        for (column in board.columns) {
            val cell = board.get(row, column)
            val imageView = tableLayout.context.cellImageView(cell)
            tableRow.addView(imageView, column)

            if (cell != null && onClick != null)
                imageView.addClickListener(scope, userState, cell, onClick)
        }
        tableLayout.addView(tableRow, row)
    }
}

private fun ImageView.addClickListener(
    scope: CoroutineScope,
    userState: UserState,
    current: Cell,
    onClick: suspend (Cell) -> Unit
) {
    setOnClickListener {
        scope.launch(Dispatchers.Main) {
            onClick(current)
        }
    }
    if (current == userState.startCell) {
        setBackgroundColor(Color.GREEN)
    }
}