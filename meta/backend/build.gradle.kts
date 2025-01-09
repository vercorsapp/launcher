/*
 * Copyright (c) 2024-2025 skyecodes
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

plugins {
    id("app.vercors.network")
    id("app.vercors.di")
    application
}

application {
    mainClass.set("app.vercors.meta.ApplicationKt")
}

dependencies {
    implementation(projects.meta.api)
    implementation(libs.bundles.ktor.server)
    implementation(libs.koin.ktor3)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.logback.classic)
    implementation(libs.kotlin.logging.jvm)
    testImplementation(libs.ktor.server.test.host)
}

tasks {
    val appProperties by registering(WriteProperties::class) {
        fun appProperty(name: String, env: String) =
            property(name, findProperty(name) as String? ?: System.getenv(env) ?: "")

        destinationFile = layout.buildDirectory.file("koin.properties")
        encoding = "UTF-8"
        appProperty("curseforgeApiKey", "CURSEFORGE_API_KEY")
        appProperty("modrinthApiKey", "MODRINTH_API_KEY")
        appProperty("microsoftClientId", "MICROSOFT_CLIENT_ID")
        appProperty("vercorsApiKey", "VERCORS_API_KEY")
    }

    processResources {
        from(appProperties)
    }
}