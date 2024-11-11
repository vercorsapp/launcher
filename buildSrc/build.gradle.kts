plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

dependencies {
    implementation(libs.kotlin.jvm)
    implementation(libs.compose)
    implementation(libs.kotlin.plugin.serialization)
    implementation(libs.kotlin.plugin.compose)
    implementation(libs.ksp)
    implementation(libs.ktorfit)
    implementation(libs.room)
    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}