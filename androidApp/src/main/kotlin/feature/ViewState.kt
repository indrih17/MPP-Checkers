package feature

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.kubsu.checkers.R
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Board
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.data.game.Score
import com.kubsu.checkers.data.game.getScore
import com.kubsu.checkers.functions.GameResult
import family.amma.keemun.StateTransform

@Immutable
data class ChechersViewState(
    val gameType: GameType,
    val board: Board,
    val selectedPiece: Cell.Piece?,
    val score: Score,
    @StringRes val message: Int
)

fun checkersStateTransform() = StateTransform<CheckersState, ChechersViewState> { state ->
    ChechersViewState(
        gameType = state.gameType,
        board = state.gameState.board,
        selectedPiece = state.startPiece,
        score = state.gameState.board.getScore(),
        message = if (state.failure != null) {
            when (state.failure) {
                is Failure.IncorrectMove -> R.string.incorrect_move
            }
        } else {
            when (state.gameState) {
                is GameState.GameOver ->
                    when (state.gameState.gameResult) {
                        GameResult.LightWon -> R.string.light_win
                        GameResult.DarkWon -> R.string.dark_win
                        GameResult.Draw -> R.string.draw
                        GameResult.NoMoves -> R.string.no_moves
                    }

                is GameState.Continues ->
                    when (state.gameState.activePlayer) {
                        CellColor.Light -> R.string.light_moves
                        CellColor.Dark -> R.string.dark_moves
                    }
            }
        }
    )
}