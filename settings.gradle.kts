pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "launcher"

include(
    "app",
    "account:data", "account:domain", "account:presentation",
    "core:data", "core:domain", "core:presentation",
    "game:data", "game:domain", "game:presentation",
    "home:data", "home:domain", "home:presentation",
    "instance:data", "instance:domain", "instance:presentation",
    "project:data", "project:domain", "project:presentation",
)
