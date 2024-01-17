package com.skyecodes.vercors.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

interface IntEnumerable {
    val value: Int
}

abstract class IntEnumerableSerializer<T : IntEnumerable>(private val values: List<T>) : KSerializer<T> {
    override val descriptor = serialDescriptor<Int>()

    override fun deserialize(decoder: Decoder): T {
        val value = decoder.decodeInt()
        return values.first { it.value == value }
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeInt(value.value)
    }
}