package com.skyecodes.vercors

import androidx.compose.ui.input.pointer.PointerIcon
import kotlinx.serialization.json.Json
import org.ocpsoft.prettytime.PrettyTime
import java.awt.Cursor
import java.awt.Desktop
import java.net.URI
import java.time.Instant

private object Utils

val appJson = Json

fun resourceAsStream(name: String) = Utils::class.java.getResourceAsStream(name)!!

fun resource(name: String) = Utils::class.java.getResource(name)!!

fun handCursor() = PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))

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

fun Instant.readable(full: Boolean = false): String = prettyTime.format(this)