package com.skyecodes.vercors.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

interface StringEnumerable {
    val value: String
}

abstract class StringEnumerableSerializer<T : StringEnumerable>(private val values: List<T>) : KSerializer<T> {
    override val descriptor = serialDescriptor<String>()

    override fun deserialize(decoder: Decoder): T {
        val value = decoder.decodeString()
        return values.first { it.value == value }
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.value)
    }
}