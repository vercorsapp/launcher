package com.skyecodes.vercors.instances.mojang

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant

typealias MojangInstant = @Serializable(MojangInstantSerializer::class) Instant

private class MojangInstantSerializer : KSerializer<MojangInstant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("MojangInstant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): MojangInstant = Instant.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: MojangInstant) {
        encoder.encodeString(value.toString())
    }
}