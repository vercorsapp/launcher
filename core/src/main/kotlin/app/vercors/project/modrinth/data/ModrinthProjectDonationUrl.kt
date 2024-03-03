package app.vercors.project.modrinth.data


import kotlinx.serialization.Serializable

@Serializable
data class ModrinthProjectDonationUrl(
    val id: String,
    val platform: String,
    val url: String
)