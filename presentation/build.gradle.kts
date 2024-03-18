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

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.jetbrains.compose)
    jacoco
}

dependencies {
    implementation(compose.runtime)
    implementation(libs.serialization.json)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.decompose)
    implementation(libs.essenty.lifecycle.coroutines)
    implementation(libs.appdirs)
    implementation(libs.kotlin.logging.jvm)
    implementation(libs.logback)
    implementation(libs.jsystemthemedetector)
    implementation(libs.coil)
    implementation(libs.coil.network.ktor)
    implementation(libs.jna)
    implementation(libs.oshi)
    implementation(project(":domain"))
    implementation(project(":core"))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
}

kotlin {
    jvmToolchain(17)
    compilerOptions.freeCompilerArgs.addAll(
        "-opt-in=kotlin.ExperimentalStdlibApi",
        "-opt-in=kotlin.io.path.ExperimentalPathApi",
        "-opt-in=kotlin.io.encoding.ExperimentalEncodingApi",
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
    )
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf("--add-opens=java.base/java.nio.file=ALL-UNNAMED")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks {
    val appProperties by registering(WriteProperties::class) {
        fun appProperty(name: String, env: String) =
            property(name, findProperty(name) as String? ?: System.getenv(env) ?: "")

        destinationFile = layout.buildDirectory.file("app.properties")
        encoding = "UTF-8"
        appProperty("curseforgeApiKey", "CURSEFORGE_API_KEY")
        appProperty("modrinthApiKey", "MODRINTH_API_KEY")
        appProperty("microsoftClientId", "MICROSOFT_CLIENT_ID")
    }

    processResources {
        from(appProperties)
    }
}