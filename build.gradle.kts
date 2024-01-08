import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.*

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

group = "com.skyecodes"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${property("serialization.version")}")
    implementation("io.ktor:ktor-client-core:${property("ktor.version")}")
    implementation("io.ktor:ktor-client-cio:${property("ktor.version")}")
    implementation("io.ktor:ktor-client-content-negotiation:${property("ktor.version")}")
    implementation("io.ktor:ktor-client-logging:${property("ktor.version")}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${property("ktor.version")}")
    implementation("cafe.adriel.voyager:voyager-navigator:${property("voyager.version")}")
    implementation("cafe.adriel.voyager:voyager-screenmodel:${property("voyager.version")}")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:${property("voyager.version")}")
    implementation("cafe.adriel.voyager:voyager-transitions:${property("voyager.version")}")
    implementation("net.harawata:appdirs:${property("appdirs.version")}")
    implementation("br.com.devsrsouza.compose.icons:font-awesome:${property("compose-icons.version")}")
    implementation("org.slf4j:slf4j-simple:2.0.10") // TODO to replace
    testImplementation(kotlin("test"))
    testImplementation("org.slf4j:slf4j-simple:2.0.10")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "com.skyecodes.snowball.MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Deb,
                TargetFormat.Rpm,
                TargetFormat.Dmg,
                TargetFormat.Pkg,
                TargetFormat.Exe,
                TargetFormat.Msi
            )
            packageName = "Snowball Launcher"
            packageVersion = version as String
            description = "Snowball Launcher"
            copyright = "Copyright (c) ${Calendar.getInstance().get(Calendar.YEAR)} skyecodes"
            vendor = "skyecodes"
            licenseFile = file("LICENSE")

            macOS {
                packageVersion = (version as String)
                    .split('.')
                    .mapIndexed { index, s -> if (index == 0) (s.toInt() + 1).toString() else s }
                    .joinToString(".")
            }
        }
    }
}
