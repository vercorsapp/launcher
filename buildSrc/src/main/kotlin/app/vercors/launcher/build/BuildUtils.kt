package app.vercors.launcher.build

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

@Deprecated("To replace")
data class ProjectModule(private val name: String) {
    val data = ":$name:data"
    val domain = ":$name:domain"
    val presentation = ":$name:presentation"
    val parent = ":$name"
}

@Deprecated("To replace") val Account = ProjectModule("account")
@Deprecated("To replace") val Core = ProjectModule("core")
@Deprecated("To replace") val Game = ProjectModule("game")
@Deprecated("To replace") val Home = ProjectModule("home")
@Deprecated("To replace") val Instance = ProjectModule("instance")
@Deprecated("To replace") val Project = ProjectModule("project")
@Deprecated("To replace") val Settings = ProjectModule("settings")
@Deprecated("To replace") val Setup = ProjectModule("setup")

@Deprecated("To replace")
fun DependencyHandlerScope.moduleImpl(module: String) {
    "implementation"(project(module))
}

@Deprecated("To replace")
fun DependencyHandlerScope.moduleImpl(module: ProjectModule) {
    moduleImpl(module.parent)
    moduleImpl(module.data)
    moduleImpl(module.domain)
    moduleImpl(module.presentation)
}

@Deprecated("To replace")
fun DependencyHandlerScope.moduleApi(module: String) {
    "api"(project(module))
}

@Deprecated("To replace")
fun DependencyHandlerScope.moduleApi(module: ProjectModule) {
    moduleApi(module.data)
    moduleApi(module.domain)
    moduleApi(module.presentation)
}