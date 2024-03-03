plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.serialization.json)
    implementation(libs.collections.immutable)
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