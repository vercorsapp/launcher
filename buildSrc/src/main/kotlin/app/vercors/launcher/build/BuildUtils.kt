package app.vercors.launcher.build

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

data class ProjectModule(private val name: String) {
    val data = ":$name:data"
    val domain = ":$name:domain"
    val presentation = ":$name:presentation"
    val parent = ":$name"
}

val Account = ProjectModule("account")
val Core = ProjectModule("core")
val Game = ProjectModule("game")
val Home = ProjectModule("home")
val Instance = ProjectModule("instance")
val Project = ProjectModule("project")

fun DependencyHandlerScope.moduleImpl(module: String) {
    "implementation"(project(module))
}

fun DependencyHandlerScope.moduleImpl(module: ProjectModule) {
    moduleImpl(module.data)
    moduleImpl(module.domain)
    moduleImpl(module.presentation)
}

fun DependencyHandlerScope.moduleApi(module: String) {
    "api"(project(module))
}

fun DependencyHandlerScope.moduleApi(module: ProjectModule) {
    moduleApi(module.data)
    moduleApi(module.domain)
    moduleApi(module.presentation)
}