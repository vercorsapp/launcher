import app.vercors.launcher.build.*
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
    moduleImpl(Account.parent)
    moduleImpl(Core.parent)
    moduleImpl(Game.parent)
    moduleImpl(Home.parent)
    moduleImpl(Instance.parent)
    moduleImpl(Project.parent)
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