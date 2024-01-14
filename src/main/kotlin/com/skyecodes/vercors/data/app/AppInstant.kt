package com.skyecodes.vercors.data.app

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant

typealias AppInstant = @Serializable(with = AppInstantSerializer::class) Instant

private class AppInstantSerializer : KSerializer<AppInstant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("AppInstant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): AppInstant = Instant.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: AppInstant) {
        encoder.encodeString(value.toString())
    }
}