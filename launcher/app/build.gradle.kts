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

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("app.vercors.launcher.data")
    id("app.vercors.launcher.presentation")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Kotlin
    implementation(libs.kotlinx.coroutines.swing)
    // Presentation
    implementation(compose.desktop.currentOs)
    // DI
    implementation(libs.koin.logger.slf4j)
    // Logging
    implementation(libs.logback.classic)
    // Modules
    implementation(projects.launcher.core)
    implementation(projects.launcher.core.config)
    implementation(projects.launcher.core.domain)
    implementation(projects.launcher.core.meta)
    implementation(projects.launcher.core.network)
    implementation(projects.launcher.core.presentation)
    implementation(projects.launcher.core.resources)
    implementation(projects.launcher.core.serialization)
    implementation(projects.launcher.core.storage)
    implementation(projects.launcher.feature)
    implementation(projects.launcher.feature.account)
    implementation(projects.launcher.feature.account.data)
    implementation(projects.launcher.feature.account.domain)
    implementation(projects.launcher.feature.account.presentation)
    implementation(projects.launcher.feature.game)
    implementation(projects.launcher.feature.game.data)
    implementation(projects.launcher.feature.game.domain)
    implementation(projects.launcher.feature.game.presentation)
    implementation(projects.launcher.feature.home)
    implementation(projects.launcher.feature.home.data)
    implementation(projects.launcher.feature.home.domain)
    implementation(projects.launcher.feature.home.presentation)
    implementation(projects.launcher.feature.instance)
    implementation(projects.launcher.feature.instance.data)
    implementation(projects.launcher.feature.instance.domain)
    implementation(projects.launcher.feature.instance.presentation)
    implementation(projects.launcher.feature.project)
    implementation(projects.launcher.feature.project.data)
    implementation(projects.launcher.feature.project.domain)
    implementation(projects.launcher.feature.project.presentation)
    implementation(projects.launcher.feature.settings)
    implementation(projects.launcher.feature.settings.data)
    implementation(projects.launcher.feature.settings.domain)
    implementation(projects.launcher.feature.settings.presentation)
    implementation(projects.launcher.feature.setup)
    implementation(projects.launcher.feature.setup.data)
    implementation(projects.launcher.feature.setup.domain)
    implementation(projects.launcher.feature.setup.presentation)
}

tasks {
    val appProperties by registering(WriteProperties::class) {
        fun appProperty(name: String, env: String) =
            property(name, findProperty(name) as String? ?: System.getenv(env) ?: "")

        destinationFile = layout.buildDirectory.file("koin.properties")
        encoding = "UTF-8"
        appProperty("curseforgeApiKey", "CURSEFORGE_API_KEY")
        appProperty("microsoftClientId", "MICROSOFT_CLIENT_ID")
        appProperty("vercorsApiKey", "VERCORS_API_KEY")
        appProperty("vercorsApiUrl", "VERCORS_API_URL")
    }

    processResources {
        from(appProperties)
    }
}

compose {
    desktop {
        application {
            mainClass = "app.vercors.launcher.app.MainKt"
            jvmArgs += listOf("-Xmx200M")

            nativeDistributions {
                modules("java.instrument", "java.management", "java.prefs", "jdk.security.auth", "jdk.unsupported")
                packageName = "vercors-launcher"
                packageVersion = rootProject.version as String
                targetFormats(TargetFormat.Deb, TargetFormat.Rpm, TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Dmg)

                buildTypes.release.proguard {
                    configurationFiles.from(file("proguard-rules.pro"))
                    isEnabled = true
                }
            }
        }
    }
}