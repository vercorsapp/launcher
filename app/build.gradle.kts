import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Kotlin
    testImplementation(kotlin("test"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.swing)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    // Presentation
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.components.resources)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    // Data
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    // DI
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.logger.slf4j)
    compileOnly(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)
    testImplementation(libs.koin.test)
    // Logging
    implementation(libs.kotlin.logging.jvm)
    implementation(libs.logback.classic)
}

tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
    compilerOptions.freeCompilerArgs.addAll(
        "-opt-in=kotlin.uuid.ExperimentalUuidApi",
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    )
}

compose {
    desktop {
        application {
            mainClass = "app.vercors.launcher.MainKt"

            nativeDistributions {
                targetFormats(*TargetFormat.values())
                packageName = "launcher"
                packageVersion = rootProject.version as String
            }
        }
    }

    resources {
        publicResClass = true
        packageOfResClass = "app.vercors.launcher.generated.resources"
    }
}

ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
}