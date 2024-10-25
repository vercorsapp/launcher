import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
    id("com.google.devtools.ksp")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

val Project.libs get() = the<LibrariesForLibs>()

dependencies {
    // Kotlin
    testImplementation(kotlin("test"))
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(compose.runtime)
    // DI
    implementation(libs.koin.core)
    compileOnly(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)
    testImplementation(libs.koin.test)
    // Logging
    implementation(libs.kotlin.logging.jvm)
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
    compilerOptions.freeCompilerArgs.addAll(
        "-opt-in=kotlin.uuid.ExperimentalUuidApi",
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    )
}