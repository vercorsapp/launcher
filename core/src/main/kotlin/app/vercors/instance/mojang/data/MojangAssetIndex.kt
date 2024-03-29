package app.vercors.instance.mojang.data

import kotlinx.serialization.Serializable

@Serializable
data class MojangAssetIndex(
    val objects: Map<String, AssetObject>
) {
    @Serializable
    data class AssetObject(
        val hash: String,
        val size: Int
    )
}