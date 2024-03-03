package app.vercors.project

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ModLoader(val value: String, val text: String) {
    @SerialName("forge")
    Forge("forge", "Forge"),

    @SerialName("neoforge")
    NeoForge("neoforge", "NeoForge"),

    @SerialName("fabric")
    Fabric("fabric", "Fabric"),

    @SerialName("quilt")
    Quilt("quilt", "Quilt");

    companion object {
        const val Vanilla = "Vanilla"
    }
}
