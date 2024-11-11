package app.vercors.launcher.project.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProjectProvider(val id: String) {
    @SerialName("modrinth")
    Modrinth("modrinth"),

    @SerialName("curseforge")
    Curseforge("curseforge")
}