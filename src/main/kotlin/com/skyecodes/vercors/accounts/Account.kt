package com.skyecodes.vercors.accounts

import com.skyecodes.vercors.common.AppInstant
import kotlinx.serialization.Serializable

@Serializable
data class Account(
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

    @Serializable
    data class TokenData(
        val refreshToken: String,
        val token: String,
        val exp: AppInstant
    )
}