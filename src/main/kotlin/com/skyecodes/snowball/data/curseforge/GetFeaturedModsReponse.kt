package com.skyecodes.snowball.data.curseforge


import kotlinx.serialization.Serializable

@Serializable
data class GetFeaturedModsReponse(
    val `data`: Data
) {
    @Serializable
    data class Data(
        val featured: List<CurseforgeMod> = emptyList(),
        val popular: List<CurseforgeMod> = emptyList(),
        val recentlyUpdated: List<CurseforgeMod> = emptyList()
    )
}