import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import java.util.*

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.mockposable)
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
    implementation(libs.logback)
    implementation(libs.reveal)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor)
    implementation(libs.rebugger)
    implementation(libs.mpfilepicker)
    implementation(project(":core"))
    testImplementation(compose.desktop.uiTestJUnit4)
    testImplementation(libs.mockk)
}

tasks.test {
    useJUnit()
}

mockposable {
    plugins = listOf("mockk")
}

tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
    compilerOptions.freeCompilerArgs.addAll(
        "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
        "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
        "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
        "-opt-in=org.jetbrains.compose.resources.ExperimentalResourceApi",
    )
}

compose {
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
                packageVersion = rootProject.version as String
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