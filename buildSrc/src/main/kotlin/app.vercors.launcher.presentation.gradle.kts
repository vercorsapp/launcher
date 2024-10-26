import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("app.vercors.launcher.base")
}

val Project.libs get() = the<LibrariesForLibs>()

dependencies {
    // Presentation
    implementation(compose.material3)
    implementation(compose.components.resources)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    // DI
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
}

ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
}

compose.resources {
    publicResClass = true
    packageOfResClass =
        "app.vercors.launcher.${if (project.name == "app") "app" else project.parent?.name ?: project.name}.generated.resources"
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}