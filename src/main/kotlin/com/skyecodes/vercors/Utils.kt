package com.skyecodes.vercors

import kotlinx.serialization.json.Json
import org.ocpsoft.prettytime.PrettyTime
import java.awt.Desktop
import java.net.URI
import java.time.Instant

private class Utils

val appJson = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}

fun resourceAsStream(name: String) = Utils::class.java.getResourceAsStream(name)!!

fun resource(name: String) = Utils::class.java.getResource(name)!!

fun openURL(uri: URI) = Desktop.getDesktop().browse(uri)

private val numberFormats = listOf("" to 1e0, "k" to 1e4, "M" to 1e6, "B" to 1e9)

fun Long.readable(decimals: Int = 1): String {
    numberFormats.forEachIndexed { i, (suffix, value) ->
        if (this >= value && this < numberFormats[i + 1].second) {
            return if (i == 0) toString() else String.format("%.${decimals}f", toDouble() / value) + suffix
        }
    }
    return ""
}

private val prettyTime = PrettyTime()

fun Instant.readable(): String = prettyTime.format(this)