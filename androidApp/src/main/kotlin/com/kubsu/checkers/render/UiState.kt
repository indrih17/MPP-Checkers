package com.kubsu.checkers.render

import android.widget.TableLayout
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.UserState
import com.kubsu.checkers.functions.GameResult
import kotlinx.coroutines.CoroutineScope

data class UiState(
    val tableLayout: TableLayout,
    val scope: CoroutineScope,
    val userState: UserState,
    val updateData: (GameState) -> Unit,
    val onFail: (Failure) -> Unit,
    val endGame: (GameResult) -> Unit
)
