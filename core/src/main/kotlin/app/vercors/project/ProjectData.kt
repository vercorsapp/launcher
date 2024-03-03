package app.vercors.project

import java.time.Instant

interface ProjectData {
    val provider: ProjectProviderType
    val id: String
    val slug: String
    val name: String
    val author: String
    val description: String
    val url: String
    val logoUrl: String?
    val imageUrl: String?
    val downloads: Long
    val lastUpdated: Instant
    //val modLoaders: List<ModLoader>
}