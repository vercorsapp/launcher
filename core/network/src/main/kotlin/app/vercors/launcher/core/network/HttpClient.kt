package app.vercors.launcher.core.network

import app.vercors.launcher.core.domain.APP_AUTHOR
import app.vercors.launcher.core.domain.APP_CONTACT
import app.vercors.launcher.core.domain.APP_ID
import app.vercors.launcher.core.domain.APP_VERSION
import app.vercors.launcher.core.storage.Storage
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cache.storage.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import java.io.File

@Single
fun provideHttpClient(json: Json, storage: Storage) = HttpClient(CIO) {
    install(Logging) {
        sanitizeHeader { header -> header in listOf(HttpHeaders.Authorization, "x-api-key") }
    }
    install(ContentNegotiation) {
        json(json)
    }
    install(UserAgent) {
        agent = "$APP_AUTHOR/$APP_ID/$APP_VERSION ($APP_CONTACT)"
    }
    install(HttpCache) {
        val cacheFile = Path(storage.state.value.path, "cache", "network")
        SystemFileSystem.createDirectories(cacheFile)
        publicStorage(FileStorage(File(cacheFile.toString())))
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 5000
    }
    install(HttpRequestRetry) {
        retryOnServerErrors(1)
        retryOnException(2, true)
        constantDelay()
    }
}