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

package app.vercors.meta.game

import app.vercors.meta.MetaResponse
import app.vercors.meta.defaultConfig
import app.vercors.meta.game.mojang.MojangLatestVersion
import app.vercors.meta.game.mojang.MojangReleaseType
import app.vercors.meta.game.mojang.MojangVersion
import app.vercors.meta.game.mojang.MojangVersionManifest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.protobuf.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test
import java.time.Instant
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation

class GameRoutesTest {
    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun test() = testApplication {
        defaultConfig()
        externalServices {
            hosts("https://piston-meta.mojang.com") {
                install(ContentNegotiation) {
                    json()
                }
                routing {
                    get("mc/game/version_manifest_v2.json") {
                        call.respond(
                            MojangVersionManifest(
                                latest = MojangLatestVersion(
                                    release = "a",
                                    snapshot = "b"
                                ),
                                versions = listOf(
                                    MojangVersion(
                                        complianceLevel = 0,
                                        id = "a",
                                        releaseTime = Instant.now(),
                                        sha1 = "",
                                        time = Instant.now(),
                                        type = MojangReleaseType.Release,
                                        url = ""
                                    ),
                                    MojangVersion(
                                        complianceLevel = 0,
                                        id = "b",
                                        releaseTime = Instant.now(),
                                        sha1 = "",
                                        time = Instant.now(),
                                        type = MojangReleaseType.Snapshot,
                                        url = ""
                                    )
                                )
                            )
                        )
                    }
                }
            }
        }
        val httpClient = createClient {
            install(ClientContentNegotiation) {
                protobuf()
            }
        }
        val response = MetaResponse.parseFrom(httpClient.get("v1/game").bodyAsBytes())
        println(response)
        delay(10000)
    }
}