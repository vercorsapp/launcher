package com.skyecodes.vercors.data.app

import kotlinx.serialization.Serializable

@Serializable
data class Instance(
    val name: String,
    val gameVersion: String,
    val loader: Loader? = null,
    val loaderVersion: String? = null,
    val created: AppInstant,
    val lastLaunched: AppInstant? = null
)
