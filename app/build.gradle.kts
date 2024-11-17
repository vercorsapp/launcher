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
    implementation(projects.core)
    implementation(projects.core.config)
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.core.presentation)
    implementation(projects.core.serialization)
    implementation(projects.core.storage)
    implementation(projects.feature.account)
    implementation(projects.feature.account.data)
    implementation(projects.feature.account.domain)
    implementation(projects.feature.account.presentation)
    implementation(projects.feature.game)
    implementation(projects.feature.game.data)
    implementation(projects.feature.game.domain)
    implementation(projects.feature.game.presentation)
    implementation(projects.feature.home)
    implementation(projects.feature.home.data)
    implementation(projects.feature.home.domain)
    implementation(projects.feature.home.presentation)
    implementation(projects.feature.instance)
    implementation(projects.feature.instance.data)
    implementation(projects.feature.instance.domain)
    implementation(projects.feature.instance.presentation)
    implementation(projects.feature.project)
    implementation(projects.feature.project.data)
    implementation(projects.feature.project.domain)
    implementation(projects.feature.project.presentation)
    implementation(projects.feature.settings)
    implementation(projects.feature.settings.data)
    implementation(projects.feature.settings.domain)
    implementation(projects.feature.settings.presentation)
    implementation(projects.feature.setup)
    implementation(projects.feature.setup.data)
    implementation(projects.feature.setup.domain)
    implementation(projects.feature.setup.presentation)
}

tasks {
    val appProperties by registering(WriteProperties::class) {
        fun appProperty(name: String, env: String) =
            property(name, findProperty(name) as String? ?: System.getenv(env) ?: "")

        destinationFile = layout.buildDirectory.file("koin.properties")
        encoding = "UTF-8"
        appProperty("curseforgeApiKey", "CURSEFORGE_API_KEY")
        appProperty("modrinthApiKey", "MODRINTH_API_KEY")
        appProperty("microsoftClientId", "MICROSOFT_CLIENT_ID")
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
                targetFormats(TargetFormat.AppImage)
                modules("java.instrument", "java.management", "java.prefs", "jdk.security.auth", "jdk.unsupported")
                packageName = "vercors-launcher"
                packageVersion = rootProject.version as String

                buildTypes.release.proguard {
                    configurationFiles.from(file("proguard-rules.pro"))
                    isEnabled = true
                }
            }
        }
    }
}