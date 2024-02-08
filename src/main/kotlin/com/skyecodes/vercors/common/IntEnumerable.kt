package com.skyecodes.vercors.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
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
        return values.firstOrNull { it.value == value }
            ?: throw SerializationException("Unable to decode value $value. Valid values are $values")
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeInt(value.value)
    }
}