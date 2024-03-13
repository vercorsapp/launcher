import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.mockposable) apply false
    alias(libs.plugins.sonarqube)
}

group = "app.vercors"
version = "0.1.0-SNAPSHOT"

subprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            if (project.findProperty("composeCompilerReports") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${
                        project.layout.buildDirectory.dir(
                            "compose_compiler"
                        ).get().asFile
                    }"
                )
            }
            if (project.findProperty("composeCompilerMetrics") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${
                        project.layout.buildDirectory.dir(
                            "compose_compiler"
                        ).get().asFile
                    }"
                )
            }
            freeCompilerArgs += listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=${
                    rootProject.layout.projectDirectory.file(
                        "compose_compiler_config.conf"
                    ).asFile
                }",
                "-opt-in=com.arkivanov.decompose.ExperimentalDecomposeApi",
            )
        }
    }

    tasks.withType<JacocoReport> {
        reports {
            xml.required = true
        }
    }
}

sonar {
    properties {
        property("sonar.projectKey", "vercorsapp_launcher_8b12075b-c1a4-4c9c-ab16-1c10c239b9e2")
        property("sonar.projectName", "launcher")
    }
}
