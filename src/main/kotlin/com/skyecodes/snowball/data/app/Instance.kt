package com.skyecodes.snowball.data.app

import kotlinx.serialization.Serializable

@Serializable
data class Instance(
    val name: String,
    val version: String,
    val loader: ModLoader?,
    val loaderVersion: String?,
    val created: AppInstant,
    val lastLaunched: AppInstant
)
