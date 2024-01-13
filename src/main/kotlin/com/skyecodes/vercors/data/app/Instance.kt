package com.skyecodes.vercors.data.app

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
