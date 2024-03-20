/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.instance.mojang

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

@Serializable(ArgumentSerializer::class)
sealed class MojangArgument

@Serializable
data class BasicArgument(
    val value: String
) : MojangArgument()

@Serializable
data class ConditionalArgument(
    val rules: List<MojangVersionInfo.Rule>,
    val value: List<String>
) : MojangArgument()

private class ArgumentSerializer : KSerializer<MojangArgument> {
    override val descriptor = buildClassSerialDescriptor("Argument")

    override fun deserialize(decoder: Decoder): MojangArgument {
        val jsonDecoder = decoder as JsonDecoder
        val element = jsonDecoder.decodeJsonElement()
        return if (element is JsonPrimitive) BasicArgument(element.content)
        else {
            val obj = element.jsonObject
            val rules = obj["rules"]?.jsonArray
                ?.map { jsonDecoder.json.decodeFromJsonElement(MojangVersionInfo.Rule.serializer(), it.jsonObject) }
            val valueJson = obj["value"]
            val value =
                if (valueJson is JsonArray) valueJson.map { it.jsonPrimitive.content }
                else listOf(valueJson!!.jsonPrimitive.content)
            ConditionalArgument(rules!!, value)
        }
    }

    override fun serialize(encoder: Encoder, value: MojangArgument) {
        // Serialization of this type is not necessary
    }
}