package com.skyecodes.vercors.projects.curseforge

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant

typealias CurseforgeInstant = @Serializable(CurseforgeInstantSerializer::class) Instant

private class CurseforgeInstantSerializer : KSerializer<CurseforgeInstant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CurseforgeInstant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): CurseforgeInstant = Instant.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: CurseforgeInstant) {}
}