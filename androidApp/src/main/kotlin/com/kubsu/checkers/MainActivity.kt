package com.kubsu.checkers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.functions.BOARD_SIZE
import family.amma.keemun.androidConnectors
import family.amma.keemun.feature.Feature
import feature.ChechersViewState
import feature.ExternalMsg
import feature.checkersFeatureParams
import feature.checkersStateTransform

class MainActivity : ComponentActivity() {
    private val feature: Feature<ChechersViewState, ExternalMsg> by androidConnectors(
        featureParams = ::checkersFeatureParams,
        getStateTransform = ::checkersStateTransform,
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
            val selectedPieceState = remember { mutableStateOf<Cell.Piece?>(null) }
            val viewState: ChechersViewState? by feature.states.collectAsState(initial = null)
            CheckersTheme {
                viewState?.let { state ->
                    LaunchedEffect(state.board) {
                        stateMatrix.update(state.board)
                    }
                    LaunchedEffect(state.selectedPiece) {
                        selectedPieceState.value = state.selectedPiece
                    }

                    BoxWithConstraints {
                        val cellSize = remember(maxWidth, maxHeight) { minOf(maxHeight, maxWidth) / BOARD_SIZE }
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
                                    score = state.score
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun Content(
        stateMatrix: StateMatrix,
        cellSize: Dp,
        selectedPieceState: MutableState<Cell.Piece?>,
        score: Score,
        @StringRes message: Int
    ) {
        Text(
            text = "${score.dark}:${score.light}",
            fontSize = 20.sp
        )
        CheckersUi(
            stateMatrix = stateMatrix,
            cellSize = cellSize,
            selectedPieceState = selectedPieceState,
            onCellClicked = onCellClicked
        )
        Text(
            text = stringResource(id = message),
            fontSize = 20.sp
        )
    }
}
