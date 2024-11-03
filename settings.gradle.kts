pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "launcher"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    "app",
    "core:serialization", "core:network", "core:storage", "core:config", "core:domain", "core:presentation",
    "account:data", "account:domain", "account:presentation",
    "game:data", "game:domain", "game:presentation",
    "home:data", "home:domain", "home:presentation",
    "instance:data", "instance:domain", "instance:presentation",
    "project:data", "project:domain", "project:presentation",
    "settings:data", "settings:domain", "settings:presentation",
    "setup:data", "setup:domain", "setup:presentation"
)
