package app.vercors.common

import kotlinx.serialization.json.Json

val AppJson = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    explicitNulls = false
    prettyPrint = true
}