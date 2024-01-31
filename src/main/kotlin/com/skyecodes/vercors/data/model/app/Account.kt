package com.skyecodes.vercors.data.model.app

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
        msAccessToken: String,
        msRefreshToken: String,
        mcAccessToken: String,
        mcTokenExpiration: AppInstant
    ) : this(name, uuid, TokenData(msAccessToken, msRefreshToken, mcAccessToken, mcTokenExpiration))

    @Serializable
    data class TokenData(
        val msAccessToken: String,
        val msRefreshToken: String,
        val mcAccessToken: String,
        val mcTokenExpiration: AppInstant
    )
}