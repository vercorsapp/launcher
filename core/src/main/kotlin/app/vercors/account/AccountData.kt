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

package app.vercors.account

import app.vercors.common.AppInstant
import io.ktor.util.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

@Serializable
data class AccountData(
    val name: String,
    val uuid: String,
    val tokenData: TokenData
) {
    constructor(
        name: String,
        uuid: String,
        msRefreshToken: String,
        mcAccessToken: String,
        mcTokenExpiration: AppInstant
    ) : this(name, uuid, TokenData(msRefreshToken, mcAccessToken, mcTokenExpiration))

    @Serializable(TokenDataSerializer::class)
    data class TokenData(
        val refreshToken: String,
        val token: String,
        val exp: AppInstant
    )

    private class TokenDataSerializer : KSerializer<TokenData> {
        override val descriptor = PrimitiveSerialDescriptor("TokenData", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): TokenData {
            val json = (decoder as JsonDecoder).json
            val jsonObject = json.decodeFromString<JsonObject>(decoder.decodeString().decodeBase64String())
            val refreshToken = jsonObject.getValue("refreshToken").jsonPrimitive.content
            val token = jsonObject.getValue("token").jsonPrimitive.content
            val exp = json.decodeFromString<AppInstant>(jsonObject.getValue("exp").jsonPrimitive.content)
            return TokenData(refreshToken, token, exp)
        }

        override fun serialize(encoder: Encoder, value: TokenData) {
            val json = (encoder as JsonEncoder).json
            val jsonObject = buildJsonObject {
                put("refreshToken", value.refreshToken)
                put("token", value.token)
                put("exp", json.encodeToString(value.exp))
            }
            return encoder.encodeString(json.encodeToString(jsonObject).encodeBase64())
        }
    }
}