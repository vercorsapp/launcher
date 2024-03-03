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