package feature

import android.os.Bundle
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.game.GameState
import com.kubsu.checkers.feature.CheckersState
import com.kubsu.checkers.feature.GameType
import family.amma.keemun.BundleFuns
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.Json

private const val GAME_TYPE_KEY = "gameType"
private const val GAME_STATE_KEY = "gameState"
private const val START_PIECE_KEY = "startPiece"
private const val FAILURE_KEY = "failure"

fun checkersStateBundleFuns() = BundleFuns(
    toBundle = { state ->
        Bundle().apply {
            putInt(GAME_TYPE_KEY, state.gameType.ordinal)
            putString(GAME_STATE_KEY, Json.encodeToString(GameState.serializer(), state.gameState))
            putString(START_PIECE_KEY, Json.encodeToString(Cell.Piece.serializer().nullable, state.startPiece))
            putString(FAILURE_KEY, Json.encodeToString(Failure.serializer().nullable, state.failure))
        }
    },
    fromBundle = { bundle: Bundle ->
        CheckersState(
            gameType = GameType.values()[bundle.getInt(GAME_TYPE_KEY)],
            gameState = Json.decodeFromString(
                deserializer = GameState.serializer(),
                string = bundle.getString(GAME_STATE_KEY) ?: error("GameState in parcel == null")
            ),
            startPiece = Json.decodeFromString(
                deserializer = Cell.Piece.serializer().nullable,
                string = bundle.getString(START_PIECE_KEY, null)
            ),
            failure = Json.decodeFromString(
                deserializer = Failure.serializer().nullable,
                string = bundle.getString(FAILURE_KEY, null)
            ),
        )
    }
)
