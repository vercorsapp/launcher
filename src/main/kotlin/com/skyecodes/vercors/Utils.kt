package com.skyecodes.vercors

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.ocpsoft.prettytime.PrettyTime
import java.awt.Desktop
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.InetAddress
import java.net.URI
import java.net.URL
import java.nio.file.Path
import java.security.MessageDigest
import java.time.Instant
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.swing.SwingUtilities
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.io.path.*


private class Utils

private val logger = KotlinLogging.logger { }

@OptIn(ExperimentalSerializationApi::class)
val AppJson = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    explicitNulls = false
    prettyPrint = true
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

fun LazyGridScope.header(
    key: Any?,
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(key = key, span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

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

fun String.sha256(): ByteArray = toByteArray().hash("SHA-256")

fun ByteArray.sha1(): ByteArray = hash("SHA-1")

private fun ByteArray.hash(method: String): ByteArray = MessageDigest.getInstance(method).digest(this)

@OptIn(ExperimentalEncodingApi::class)
fun ByteArray.encodeBase64(): String = Base64.UrlSafe.encode(this)

@OptIn(ExperimentalStdlibApi::class)
fun ByteArray.encodeHex(): String = toHexString()

suspend fun validate(path: Path, url: String, sha1: String? = null, size: Int? = null, attempts: Int = 3) =
    coroutineScope {
        logger.trace { "Validating file at location $path" }
        repeat(attempts) {
            if (!path.exists()) {
                logger.debug { "File at location $path does not exist - Downloading it from $url" }
                URL(url).openStream()
                    .use { i -> path.createParentDirectories().outputStream().use { o -> i.copyTo(o) } }
            }

            if (sha1 == null && size == null) {
                return@coroutineScope
            }

            val fileBytes = path.readBytes()
            var complete = true

            if (sha1 != null) {
                val fileSha1 = fileBytes.sha1().encodeHex()
                if (fileSha1 != sha1) {
                    logger.warn { "SHA1 doesn't match $sha1 for file $path" }
                    complete = false
                }
            }

            if (complete && size != null) {
                val fileSize = fileBytes.size
                if (fileSize != size) {
                    logger.warn { "File size doesn't match $size for file $path" }
                    complete = false
                }
            }

            if (complete) {
                return@coroutineScope
            } else {
                logger.warn { "Validation for file $path failed - retrying" }
                path.deleteExisting()
            }
            delay(1000L)
        }
        logger.error { "Couldn't validate file at location $path after $attempts attempts - aborting" }
        throw FetchException("Could not fetch or/and validate file at URL $url and path $path")
    }

suspend fun unzipFile(src: Path, dest: Path, exclude: List<String>) = coroutineScope {
    logger.trace { "Unzipping file $src to location $dest" }
    val zis = ZipInputStream(FileInputStream(src.toFile()))
    var zipEntry = zis.nextEntry
    while (zipEntry != null) {
        if (exclude.none { zipEntry!!.name.startsWith(it) }) {
            val newFile = newFile(dest, zipEntry)
            if (zipEntry.isDirectory) {
                if (!newFile.isDirectory()) newFile.createDirectories()
            } else {
                val parent = newFile.parent
                if (!parent.isDirectory()) parent.createDirectories()
                newFile.outputStream().use { zis.copyTo(it) }
            }
        }
        zipEntry = zis.nextEntry
    }
    zis.closeEntry()
    zis.close()
}

fun String.withQuotes() = "\"$this\""

@Throws(IOException::class)
private fun newFile(destinationDir: Path, zipEntry: ZipEntry): Path {
    val destFile = destinationDir.resolve(zipEntry.name)

    val destDirPath = destinationDir.absolutePathString()
    val destFilePath = destFile.absolutePathString()

    if (!destFilePath.startsWith(destDirPath + File.separator)) {
        throw IOException("Entry is outside of the target dir: " + zipEntry.name)
    }

    return destFile
}

class FetchException(message: String) : Exception(message)

fun appHttpClient(json: Json) = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(json)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 3000
    }
    install(HttpRequestRetry) {
        retryOnServerErrors(1)
        retryOnException(1, true)
        constantDelay()
    }
    install(UserAgent) {
        agent = "skyecodes/vercors/$APP_VERSION (contact@skye.codes)"
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.INFO
    }
}

inline fun <reified T> HttpRequestBuilder.jsonBody(body: T) {
    setBody(body)
    contentType(ContentType.Application.Json)
    expectSuccess = true
}

suspend fun isInternetAvailable(): Boolean = withContext(Dispatchers.IO) {
    try {
        val address = InetAddress.getByName("api.mojang.com")
        address != null && !address.equals("") && address.isReachable(5000)
    } catch (e: IOException) {
        false
    }
}