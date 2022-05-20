package feature

import android.os.Parcel
import android.os.Parcelable
import com.kubsu.checkers.data.Failure
import com.kubsu.checkers.data.entities.Cell
import com.kubsu.checkers.data.entities.CellColor
import com.kubsu.checkers.data.game.GameState
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import kotlinx.serialization.json.Json

@Parcelize
data class CheckersState(
    val gameType: GameType,
    val gameState: @WriteWith<GameStateParceler> GameState,
    val startPiece: @WriteWith<CellParceler> Cell.Piece?,
    val failure: @WriteWith<FailureParceler> Failure?
) : Parcelable

sealed class GameType : Parcelable {
    @Parcelize
    object HumanVsAi : GameType()

    @Parcelize
    object HumanVsHuman : GameType()

    @Parcelize
    object AiVsAi : GameType()
}

enum class ActivePlayer { Human, AI }

fun activePlayer(playerColor: CellColor, gameType: GameType): ActivePlayer =
    when (gameType) {
        GameType.HumanVsHuman -> ActivePlayer.Human
        GameType.AiVsAi -> ActivePlayer.AI
        GameType.HumanVsAi ->
            when (playerColor) {
                CellColor.Dark -> ActivePlayer.AI
                CellColor.Light -> ActivePlayer.Human
            }
    }

private object GameStateParceler : Parceler<GameState> {
    override fun create(parcel: Parcel): GameState =
        Json.decodeFromString(GameState.serializer(), parcel.readString() ?: error("GameState in parcel == null"))

    override fun GameState.write(parcel: Parcel, flags: Int) {
        parcel.writeString(Json.encodeToString(GameState.serializer(), this))
    }
}

private object CellParceler : Parceler<Cell.Piece?> {
    override fun create(parcel: Parcel): Cell.Piece? = null
    override fun Cell.Piece?.write(parcel: Parcel, flags: Int) = Unit
}

private object FailureParceler : Parceler<Failure?> {
    override fun create(parcel: Parcel): Failure? = null
    override fun Failure?.write(parcel: Parcel, flags: Int) = Unit
}
