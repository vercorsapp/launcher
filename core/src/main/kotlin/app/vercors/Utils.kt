package app.vercors

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.awt.Desktop
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.InetAddress
import java.net.URI
import java.net.URL
import java.nio.file.Path
import java.security.MessageDigest
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.io.path.*

private class Utils

private val logger = KotlinLogging.logger { }

const val APP_VERSION = "0.1.0"
const val APP_NAME = "Vercors"

@OptIn(ExperimentalSerializationApi::class)
val AppJson = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    explicitNulls = false
    prettyPrint = true
}

fun loadProperties() = Properties().apply { load(resourceAsStream("/app.properties")) }

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



fun <T> T.applyIf(condition: Boolean, runnable: T.() -> T): T = if (condition) run(runnable) else this

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

            val fileBytes = path.readBytes()
            val sha1Ok = checkSha1(fileBytes, sha1)
            if (!sha1Ok) logger.warn { "SHA1 doesn't match $sha1 for file $path" }
            val sizeOk = checkSize(fileBytes, size)
            if (!sizeOk) logger.warn { "File size doesn't match $size for file $path" }

            if (sha1Ok && sizeOk) {
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

private fun checkSha1(fileBytes: ByteArray, sha1: String?): Boolean {
    if (sha1 != null) {
        val fileSha1 = fileBytes.sha1().encodeHex()
        if (fileSha1 != sha1) return false
    }
    return true
}

private fun checkSize(fileBytes: ByteArray, size: Int?): Boolean {
    if (size != null) {
        val fileSize = fileBytes.size
        if (fileSize != size) return false
    }
    return true
}

suspend fun unzipFile(src: Path, dest: Path, exclude: List<String>) = coroutineScope {
    logger.trace { "Unzipping file $src to location $dest" }
    val zis = ZipInputStream(FileInputStream(src.toFile()))
    zis.use {
        var zipEntry = zis.nextEntry
        while (zipEntry != null) {
            if (exclude.none { zipEntry!!.name.startsWith(it) }) {
                processZipEntry(zis, zipEntry, dest)
            }
            zipEntry = zis.nextEntry
        }
        zis.closeEntry()
    }
}

private suspend fun processZipEntry(
    zis: ZipInputStream,
    zipEntry: ZipEntry,
    dest: Path,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Unit = withContext(dispatcher) {
    val newFile = createFile(dest, zipEntry, dispatcher)
    if (zipEntry.isDirectory) {
        if (!newFile.isDirectory()) newFile.createDirectories()
    } else {
        val parent = newFile.parent
        if (!parent.isDirectory()) parent.createDirectories()
        newFile.outputStream().use { zis.copyTo(it) }
    }
}

@Throws(IOException::class)
private suspend fun createFile(
    destinationDir: Path,
    zipEntry: ZipEntry,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Path = withContext(dispatcher) {
    val destFile = destinationDir.resolve(zipEntry.name)
    val destDirPath = destinationDir.absolutePathString()
    val destFilePath = destFile.absolutePathString()
    if (!destFilePath.startsWith(destDirPath + File.separator))
        throw IOException("Entry is outside of the target dir: " + zipEntry.name)
    destFile
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

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Deferred<T>.onSuccessUse(block: Deferred<T>.(T) -> Unit) = apply {
    invokeOnCompletion { if (it == null) block(getCompleted()) }
}