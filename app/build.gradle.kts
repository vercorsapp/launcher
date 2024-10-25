import app.vercors.launcher.build.*
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
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
    moduleImpl(Account.Parent)
    moduleImpl(Core.Parent)
    moduleImpl(Game.Parent)
    moduleImpl(Home.Parent)
    moduleImpl(Instance.Parent)
    moduleImpl(Project.Parent)
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

    resources {
        publicResClass = true
        packageOfResClass = "app.vercors.launcher.generated.resources"
    }
}