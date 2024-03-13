import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

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
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf("--add-opens=java.base/java.nio.file=ALL-UNNAMED")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
    compilerOptions.freeCompilerArgs.addAll(
        "-opt-in=kotlin.ExperimentalStdlibApi",
        "-opt-in=kotlin.io.path.ExperimentalPathApi",
        "-opt-in=kotlin.io.encoding.ExperimentalEncodingApi",
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
    )
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