package com.skyecodes.snowball.data.modrinth

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant

typealias ModrinthInstant = @Serializable(with = ModrinthInstantSerializer::class) Instant

private class ModrinthInstantSerializer : KSerializer<ModrinthInstant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ModrinthInstant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ModrinthInstant = Instant.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ModrinthInstant) {}
}