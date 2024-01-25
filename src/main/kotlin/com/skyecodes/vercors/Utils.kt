package com.skyecodes.vercors

import androidx.compose.runtime.Stable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.ocpsoft.prettytime.PrettyTime
import java.awt.Desktop
import java.net.URI
import java.time.Instant
import javax.swing.SwingUtilities

private class Utils

@OptIn(ExperimentalSerializationApi::class)
val AppJson = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    explicitNulls = false
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

@Stable
fun Instant.readable(): String = prettyTime.format(this)

fun <T> T.applyIf(condition: Boolean, runnable: T.() -> T): T = if (condition) run(runnable) else this

fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}