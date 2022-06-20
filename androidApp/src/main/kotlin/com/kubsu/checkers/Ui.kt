package com.kubsu.checkers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor

@Composable
@Stable
fun CheckersUi(
    stateMatrix: StateMatrix,
    cellSize: Dp,
    selectedPieceState: MutableState<Cell.Piece?>,
    onCellClicked: (Cell) -> Unit
) {
    LazyColumn {
        items(stateMatrix) { cells ->
            LazyRow {
                items(cells) { cellState ->
                    if (cellState != null) {
                        CellUi(
                            cellState = cellState,
                            selectedPieceState = selectedPieceState,
                            size = cellSize,
                            onCellClicked = onCellClicked
                        )
                    } else {
                        WhiteArea(cellSize)
                    }
                }
            }
        }
    }
}

@Composable
@Stable
private fun CellUi(
    cellState: MutableState<Cell>,
    selectedPieceState: MutableState<Cell.Piece?>,
    size: Dp,
    onCellClicked: (Cell) -> Unit
) {
    Image(
        modifier = Modifier
            .size(size)
            .drawBehind {
                drawRect(if (selectedPieceState.value == cellState.value) Color.Green else Color.Black)
            }
            .clickable { onCellClicked(cellState.value) },
        painter = painterResource(id = drawable(cellState.value)),
        contentDescription = null // TODO
    )
}

@Composable
private fun WhiteArea(size: Dp) {
    Image(
        modifier = Modifier.size(size),
        painter = painterResource(id = R.drawable.white_cell),
        contentDescription = null // TODO
    )
}

@DrawableRes
@Stable
private fun drawable(cell: Cell) =
    when (cell) {
        is Cell.Piece.Man ->
            if (cell.color == CellColor.Light) R.drawable.white_man else R.drawable.black_man
        is Cell.Piece.King ->
            if (cell.color == CellColor.Light) R.drawable.white_king else R.drawable.black_king
        is Cell.Empty ->
            R.drawable.black_cell
    }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    closeOnClickToEmptyArea: Boolean = false,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = closeOnClickToEmptyArea),
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 4.dp).let { modifier ->
                if (closeOnClickToEmptyArea) {
                    modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismissRequest
                    )
                } else {
                    modifier
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .background(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface)
                    .then(modifier),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
                content = content
            )
        }
    }
}
