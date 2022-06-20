package com.kubsu.checkers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.feature.ExternalMsg
import com.kubsu.checkers.feature.GameType
import com.kubsu.checkers.feature.checkersEffectHandler
import com.kubsu.checkers.feature.checkersFeatureParams
import com.kubsu.checkers.functions.BOARD_SIZE
import family.amma.keemun.androidConnectors
import family.amma.keemun.feature.Feature
import feature.*
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    private val feature: Feature<ChechersViewState, ExternalMsg> by androidConnectors(
        featureParams = {
            val gameTypeDataStore = gameTypeDataStore
            checkersFeatureParams(
                preEffect = { gameTypeDataStore.data.first() },
                effectHandler = checkersEffectHandler(
                    updateGameType = { gameType -> gameTypeDataStore.updateData { gameType }}
                )
            )
        },
        getStateTransform = ::checkersStateTransform,
        onSaveState = checkersStateBundleFuns()
    )

    /**
     * In order to @Stable the lambda and compose functions were not recomposed.
     */
    private val onCellClicked: (Cell) -> Unit = { feature dispatch ExternalMsg.CellSelected(it) }

    override fun onBackPressed() =
        finish()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val stateMatrix = stateMatrix()
            val selectedPieceState: MutableState<Cell.Piece?> = remember { mutableStateOf(null) }
            val viewState: ChechersViewState? by feature.states.collectAsState(initial = null)
            CheckersTheme {
                viewState?.let { state ->
                    LaunchedEffect(state.board) {
                        stateMatrix.update(state.board)
                    }
                    LaunchedEffect(state.selectedPiece) {
                        selectedPieceState.value = state.selectedPiece
                    }
                    CheckersUi(stateMatrix, selectedPieceState, state)
                }
            }
        }
    }

    @Composable
    @Stable
    private fun CheckersUi(stateMatrix: StateMatrix, selectedPieceState: MutableState<Cell.Piece?>, state: ChechersViewState) {
        BoxWithConstraints {
            val cellSize = remember(maxWidth, maxHeight) { (minOf(maxHeight, maxWidth) * 0.8f) / BOARD_SIZE }
            if (maxHeight > maxWidth) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        Content(
                            stateMatrix = stateMatrix,
                            cellSize = cellSize,
                            selectedPieceState = selectedPieceState,
                            message = state.message,
                            score = state.score,
                            gameType = state.gameType,
                        )
                    }
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    content = {
                        Content(
                            stateMatrix = stateMatrix,
                            cellSize = cellSize,
                            selectedPieceState = selectedPieceState,
                            message = state.message,
                            score = state.score,
                            gameType = state.gameType,
                        )
                    }
                )
            }
        }
    }

    @Composable
    @Stable
    private fun Content(
        stateMatrix: StateMatrix,
        cellSize: Dp,
        selectedPieceState: MutableState<Cell.Piece?>,
        score: Score,
        gameType: GameType,
        @StringRes message: Int
    ) {
        var isDialogOpened by remember { mutableStateOf(false) }
        if (isDialogOpened) {
            BottomDialog(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                closeOnClickToEmptyArea = true,
                onDismissRequest = { isDialogOpened = false }
            ) {
                GameType.values().forEach { gameType ->
                    Button(onClick = {
                        feature dispatch ExternalMsg.ChangeGameType(gameType)
                        isDialogOpened = false
                    }) {
                        Text(stringResource(id = gameType.stringRes))
                    }
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${score.dark}:${score.light}",
                fontSize = 20.sp
            )
            Text(
                text = stringResource(id = message),
                fontSize = 20.sp
            )
            TextButton(onClick = { isDialogOpened = true }) {
                Text(text = stringResource(id = gameType.stringRes))
            }
        }
        CheckersUi(
            stateMatrix = stateMatrix,
            cellSize = cellSize,
            selectedPieceState = selectedPieceState,
            onCellClicked = onCellClicked
        )
    }

    private val GameType.stringRes: Int
        @StringRes get() = when (this) {
            GameType.HumanVsAi -> R.string.h_vs_ai
            GameType.HumanVsHuman -> R.string.h_vs_h
            GameType.AiVsAi -> R.string.ai_vs_ai
        }
}
