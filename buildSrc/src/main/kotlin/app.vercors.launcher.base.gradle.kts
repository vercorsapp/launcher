import org.gradle.accessors.dm.LibrariesForLibs

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
    implementation(libs.kotlinx.io)
    implementation(compose.runtime)
    // DI
    implementation(libs.koin.core)
    compileOnly(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)
    testImplementation(libs.koin.test)
    // Logging
    implementation(libs.kotlin.logging.jvm)
}

val jdkVersion = 17

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(jdkVersion)
    }
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
    jvmToolchain(jdkVersion)
}

tasks.test {
    useJUnitPlatform()
}
