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
    "core:serialization",
    "core:network",
    "core:storage",
    "core:config",
    "core:domain",
    "core:presentation",
    "core:resources",
    "feature:account:data",
    "feature:account:domain",
    "feature:account:presentation",
    "feature:game:data",
    "feature:game:domain",
    "feature:game:presentation",
    "feature:home:data",
    "feature:home:domain",
    "feature:home:presentation",
    "feature:instance:data",
    "feature:instance:domain",
    "feature:instance:presentation",
    "feature:project:data",
    "feature:project:domain",
    "feature:project:presentation",
    "feature:settings:data",
    "feature:settings:domain",
    "feature:settings:presentation",
    "feature:setup:data",
    "feature:setup:domain",
    "feature:setup:presentation"
)
