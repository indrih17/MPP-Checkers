package com.kubsu.checkers.render

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import com.kubsu.checkers.GameType
import com.kubsu.checkers.R
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.MoveState
import com.kubsu.checkers.functions.GameResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun startGame(common: CommonData, gameType: GameType) =
    when (gameType) {
        is GameType.HumanVsHuman ->
            common.updateGame(gameType)
        is GameType.HumanVsAi ->
            common.updateGame(gameType, ActivePlayer.Human)
        is GameType.AiVsAi ->
            common.updateGame(gameType)
    }

data class CommonData(
    val tableLayout: TableLayout,
    val scope: CoroutineScope,
    val moveState: MoveState,
    val updateData: (GameState) -> Unit,
    val onFail: (Failure.IncorrectMove) -> Unit,
    val endGame: (GameResult) -> Unit
)

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