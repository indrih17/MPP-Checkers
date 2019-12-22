package com.kubsu.checkers.render

import android.widget.TableLayout
import com.kubsu.checkers.GameType
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.MoveState
import com.kubsu.checkers.functions.GameResult

internal suspend fun GameType.HumanVsAi.render(
    humanColor: CellColor,
    tableLayout: TableLayout,
    moveState: MoveState,
    updateData: (GameState) -> Unit,
    endGame: (GameResult) -> Unit
) {
}