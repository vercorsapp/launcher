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

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.*

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.mockposable)
    jacoco
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.components.resources)
    implementation(libs.coroutines.swing)
    implementation(libs.decompose)
    implementation(libs.decompose.extensions.compose)
    implementation(libs.essenty.lifecycle.coroutines)
    implementation(libs.compose.icons.feather)
    implementation(libs.prettytime)
    implementation(libs.kotlin.logging.jvm)
    implementation(libs.reveal)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor)
    implementation(libs.rebugger)
    implementation(libs.mpfilepicker)
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))
    implementation(project(":core"))
    testImplementation(compose.desktop.uiTestJUnit4)
    testImplementation(libs.mockk)
}

kotlin {
    jvmToolchain(17)
    compilerOptions.freeCompilerArgs.addAll(
        "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
        "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
        "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
        "-opt-in=org.jetbrains.compose.resources.ExperimentalResourceApi",
        "-opt-in=com.arkivanov.decompose.ExperimentalDecomposeApi"
    )
}

tasks.test {
    useJUnit()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

mockposable {
    plugins = listOf("mockk")
}

compose {
    val kotlinVersion = libs.versions.kotlin.asProvider().get()
    kotlinCompilerPlugin.set(dependencies.compiler.forKotlin(kotlinVersion))
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=$kotlinVersion")
    desktop {
        application {
            mainClass = "app.vercors.MainKt"

            nativeDistributions {
                targetFormats(
                    TargetFormat.Deb,
                    TargetFormat.Rpm,
                    TargetFormat.Dmg,
                    TargetFormat.Pkg,
                    TargetFormat.Exe,
                    TargetFormat.Msi
                )
                packageName = "Vercors"
                packageVersion = (rootProject.version as String).split("-")[0]
                description = "A modern Minecraft launcher with mod support"
                copyright = "Copyright (c) ${Calendar.getInstance().get(Calendar.YEAR)} skyecodes"
                vendor = "skyecodes"
                licenseFile = file("../LICENSE")

                modules("java.instrument", "java.management", "jdk.unsupported", "java.naming")

                windows {
                    shortcut = true
                    menu = true
                }

                linux {
                    shortcut = true
                }

                macOS {
                    packageVersion = (rootProject.version as String)
                        .split("-")[0]
                        .split('.')
                        .mapIndexed { index, s -> if (index == 0) (s.toInt() + 1).toString() else s }
                        .joinToString(".")
                }

                buildTypes.release.proguard {
                    configurationFiles.from(file("proguard-rules.pro"))
                    isEnabled = true
                }
            }
        }
    }
}