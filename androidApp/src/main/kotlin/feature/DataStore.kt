package feature

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.kubsu.checkers.feature.GameType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

typealias GameTypeDataStore = DataStore<GameType>

val Context.gameTypeDataStore: GameTypeDataStore by dataStore(
    fileName = "GameTypeDataStore",
    serializer = ProtobufSerializer(
        kSerializer = GameType.serializer(),
        defaultValue = GameType.HumanVsAi
    )
)

@OptIn(ExperimentalSerializationApi::class)
class ProtobufSerializer<T : Any>(
    private val kSerializer: KSerializer<T>,
    override val defaultValue: T,
    private val protoBuf: ProtoBuf = ProtoBuf { encodeDefaults = true }
) : Serializer<T> {
    override suspend fun readFrom(input: InputStream): T =
        protoBuf.decodeFromByteArray(kSerializer, input.readBytes())

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: T, output: OutputStream) {
        output.write(protoBuf.encodeToByteArray(kSerializer, t))
    }
}
