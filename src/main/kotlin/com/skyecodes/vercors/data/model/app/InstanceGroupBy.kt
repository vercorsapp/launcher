package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.data.model.mojang.MojangVersionManifest
import com.skyecodes.vercors.ui.Localization
import com.skyecodes.vercors.ui.UI
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class InstanceGroupBy(val localizedName: (Localization) -> String, val behavior: Behavior<*>) {
    @SerialName("none")
    None(Localization::none, Behavior.None),

    @SerialName("minecraftVersion")
    GameVersion(Localization::minecraftVersion, Behavior.GameVersion),

    @SerialName("loader")
    Loader(Localization::loader, Behavior.Loader);

    sealed class Behavior<T>(
        val key: (Instance) -> T,
        val header: (T) -> String = { it.toString() },
        val comparator: Comparator<T> = Comparator.comparing { header(it) }
    ) {
        data object None : Behavior<String>(
            key = { "" }
        )

        data object GameVersion : Behavior<MojangVersionManifest.Version>(
            key = Instance::gameVersion,
            header = MojangVersionManifest.Version::id,
            comparator = Comparator.comparing(MojangVersionManifest.Version::releaseTime)
        )

        data object Loader : Behavior<com.skyecodes.vercors.data.model.app.Loader?>(
            key = Instance::loader,
            header = { it?.text ?: UI.vanilla }
        )
    }
}