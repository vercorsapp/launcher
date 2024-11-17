import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("app.vercors.launcher.base")
}

val Project.libs get() = the<LibrariesForLibs>()

dependencies {
    // Presentation
    implementation(compose.material3)
    implementation(compose.material3AdaptiveNavigationSuite)
    implementation(compose.components.resources)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor3)
    implementation(libs.coil.network.cache.control)
    implementation(libs.filekit.compose)
    implementation(libs.prettytime)
    // DI
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
}

ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}