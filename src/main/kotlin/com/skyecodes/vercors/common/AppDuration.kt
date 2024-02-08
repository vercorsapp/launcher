package com.skyecodes.vercors.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Duration

typealias AppDuration = @Serializable(AppDurationSerializer::class) Duration

internal class AppDurationSerializer : KSerializer<AppDuration> {
    override val descriptor = PrimitiveSerialDescriptor("AppDuration", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): AppDuration {
        return Duration.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: AppDuration) {
        encoder.encodeString(value.toString())
    }
}