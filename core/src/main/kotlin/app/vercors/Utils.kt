/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import java.awt.Desktop
import java.io.IOException
import java.net.InetAddress
import java.net.URI
import java.security.MessageDigest
import java.util.*
import kotlin.io.encoding.Base64

private class Utils

private val logger = KotlinLogging.logger {}

const val APP_VERSION = "0.1.0"
const val APP_NAME = "Vercors"

fun loadProperties(file: String) = Properties().apply { load(resourceAsStream(file)) }

fun resourceAsStream(name: String) = Utils::class.java.getResourceAsStream(name)!!

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

fun <T> T.applyIf(condition: Boolean, runnable: T.() -> T): T = if (condition) run(runnable) else this

fun <T, V> T.applyNotNull(condition: V?, runnable: T.(V) -> T): T = condition?.let { runnable(it) } ?: this

fun String.sha256(): ByteArray = toByteArray().hash("SHA-256")

fun ByteArray.sha1(): ByteArray = hash("SHA-1")

private fun ByteArray.hash(method: String): ByteArray = MessageDigest.getInstance(method).digest(this)

fun ByteArray.encodeBase64(): String = Base64.UrlSafe.encode(this)

fun ByteArray.encodeHex(): String = toHexString()

suspend fun isInternetAvailable(dispatcher: CoroutineDispatcher = Dispatchers.IO): Boolean = withContext(dispatcher) {
    try {
        val address = InetAddress.getByName("api.mojang.com")
        address != null && address.isReachable(5000)
    } catch (e: IOException) {
        false
    }
}

fun Job.onError(block: Job.(Throwable) -> Unit) = apply {
    invokeOnCompletion { if (it != null && it !is CancellationException) block(it) }
}

fun Job.onSuccess(block: Job.() -> Unit) = apply {
    invokeOnCompletion { if (it == null) block() }
}

fun Job.onCancel(block: Job.(CancellationException) -> Unit) = apply {
    invokeOnCompletion { if (it is CancellationException) block(it) }
}

fun <T> Deferred<T>.onSuccessUse(block: Deferred<T>.(T) -> Unit) = apply {
    invokeOnCompletion { if (it == null) block(getCompleted()) }
}