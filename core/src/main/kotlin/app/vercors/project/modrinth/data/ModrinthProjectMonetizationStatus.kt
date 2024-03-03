package app.vercors.project.modrinth.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ModrinthProjectMonetizationStatus {
    @SerialName("monetized")
    Monetized,

    @SerialName("demonetized")
    Demonetized,

    @SerialName("force-demonetized")
    ForceDemonetized
}