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
    implementation(projects.account)
    implementation(projects.account.data)
    implementation(projects.account.domain)
    implementation(projects.account.presentation)
    implementation(projects.core)
    implementation(projects.core.config)
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.core.presentation)
    implementation(projects.core.serialization)
    implementation(projects.core.storage)
    implementation(projects.game)
    implementation(projects.game.data)
    implementation(projects.game.domain)
    implementation(projects.game.presentation)
    implementation(projects.home)
    implementation(projects.home.data)
    implementation(projects.home.domain)
    implementation(projects.home.presentation)
    implementation(projects.instance)
    implementation(projects.instance.data)
    implementation(projects.instance.domain)
    implementation(projects.instance.presentation)
    implementation(projects.project)
    implementation(projects.project.data)
    implementation(projects.project.domain)
    implementation(projects.project.presentation)
    implementation(projects.settings)
    implementation(projects.settings.data)
    implementation(projects.settings.domain)
    implementation(projects.settings.presentation)
    implementation(projects.setup)
    implementation(projects.setup.data)
    implementation(projects.setup.domain)
    implementation(projects.setup.presentation)
}

compose {
    desktop {
        application {
            mainClass = "app.vercors.launcher.app.MainKt"

            nativeDistributions {
                targetFormats(*TargetFormat.values())
                packageName = "launcher"
                packageVersion = rootProject.version as String
            }
        }
    }
}