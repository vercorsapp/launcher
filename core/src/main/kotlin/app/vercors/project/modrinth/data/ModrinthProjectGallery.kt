package app.vercors.project.modrinth.data


import kotlinx.serialization.Serializable

@Serializable
data class ModrinthProjectGallery(
    val created: String,
    val description: String,
    val featured: Boolean,
    val ordering: Int,
    val title: String,
    val url: String
)