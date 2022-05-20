package com.kubsu.checkers.data.entities

import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/** Current board state. */
typealias Board = Matrix<Cell?>

object BoardSerializer : KSerializer<Board> {
    private val boardSerializer = ListSerializer(ListSerializer(Cell.serializer().nullable))

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor = SerialDescriptor("Board", boardSerializer.descriptor)

    override fun deserialize(decoder: Decoder): Board {
        val lists = decoder.decodeSerializableValue(boardSerializer)
        return Matrix(lists.map { it.toPersistentList() }.toPersistentList())
    }

    override fun serialize(encoder: Encoder, value: Board) {
        encoder.encodeSerializableValue(boardSerializer, value.value)
    }
}

@Suppress("UNUSED")
val Board.piecesAmount: Int
    inline get() = 12

/** Updating a cell on the board. */
fun Board.update(cell: Cell): Board =
    set(cell.row, cell.column, cell)

fun Board.get(cell: Cell, increase: Increase): Cell? =
    get(cell.row + increase.rowIncrease, cell.column + increase.columnIncrease)

val Board.rows: IntRange
    get() = firstIndex..lastIndex

val Board.columns: IntRange
    get() = firstIndex..lastIndex