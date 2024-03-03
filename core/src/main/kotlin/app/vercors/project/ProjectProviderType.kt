package app.vercors.project

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProjectProviderType(val value: String, val text: String) {
    @SerialName("modrinth")
    Modrinth("modrinth", "Modrinth"),

    @SerialName("curseforge")
    Curseforge("curseforge", "Curseforge")
}
