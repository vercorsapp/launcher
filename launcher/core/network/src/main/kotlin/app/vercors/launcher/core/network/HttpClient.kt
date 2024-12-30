/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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