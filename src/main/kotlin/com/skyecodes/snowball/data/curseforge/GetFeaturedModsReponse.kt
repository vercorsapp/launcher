package com.skyecodes.snowball.data.curseforge


import kotlinx.serialization.Serializable

@Serializable
data class GetFeaturedModsReponse(
    val `data`: Data
) {
    @Serializable
    data class Data(
        val featured: List<CurseforgeProject> = emptyList(),
        val popular: List<CurseforgeProject> = emptyList(),
        val recentlyUpdated: List<CurseforgeProject> = emptyList()
    )
}