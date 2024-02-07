import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.*

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.jetbrains.compose)
}

group = "com.skyecodes"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.serialization.json)
    implementation(libs.coroutines.swing)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.decompose)
    implementation(libs.decompose.extensions.compose)
    implementation(libs.essenty.lifecycle.coroutines)
    implementation(libs.compose.icons.feather)
    implementation(libs.appdirs)
    implementation(libs.prettytime)
    implementation(libs.kotlin.logging.jvm)
    implementation(libs.koin.core)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.koin.compose)
    implementation(libs.logback)
    implementation(libs.jsystemthemedetector)
    implementation(libs.reveal)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor)
    implementation(libs.rebugger)
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    val appProperties by registering(WriteProperties::class) {
        destinationFile = layout.buildDirectory.file("app.properties")
        encoding = "UTF-8"
        property("curseforgeApiKey", findProperty("curseforgeApiKey") as String? ?: System.getenv("CURSEFORGE_API_KEY"))
        property("modrinthApiKey", findProperty("modrinthApiKey") as String? ?: System.getenv("MODRINTH_API_KEY"))
        property(
            "microsoftClientId",
            findProperty("microsoftClientId") as String? ?: System.getenv("MICROSOFT_CLIENT_ID")
        )
    }

    processResources {
        from(appProperties)
    }
}


compose.desktop {
    application {
        mainClass = "com.skyecodes.vercors.MainKt"

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
            packageVersion = version as String
            description = "A modern Minecraft launcher with mod support"
            copyright = "Copyright (c) ${Calendar.getInstance().get(Calendar.YEAR)} skyecodes"
            vendor = "skyecodes"
            licenseFile = file("LICENSE")

            modules("java.instrument", "java.management", "jdk.unsupported", "java.naming")

            windows {
                shortcut = true
                menu = true
            }

            linux {
                shortcut = true
            }

            macOS {
                packageVersion = (version as String)
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
