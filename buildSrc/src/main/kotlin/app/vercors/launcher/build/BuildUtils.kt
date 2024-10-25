package app.vercors.launcher.build

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

data class ProjectModule(private val name: String) {
    val Data = ":$name:data"
    val Domain = ":$name:domain"
    val Presentation = ":$name:presentation"
    val Parent = ":$name"
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
    moduleImpl(module.Data)
    moduleImpl(module.Domain)
    moduleImpl(module.Presentation)
}

fun DependencyHandlerScope.moduleApi(module: String) {
    "api"(project(module))
}

fun DependencyHandlerScope.moduleApi(module: ProjectModule) {
    moduleApi(module.Data)
    moduleApi(module.Domain)
    moduleApi(module.Presentation)
}