package app.vercors.instance

import app.vercors.instance.mojang.data.MojangVersionManifest
import app.vercors.project.ModLoader
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class InstanceGroupBy(val behavior: Behavior<*>) {
    @SerialName("none")
    None(Behavior.None),

    @SerialName("gameVersion")
    GameVersion(Behavior.GameVersion),

    @SerialName("loader")
    Loader(Behavior.Loader);

    sealed class Behavior<T>(
        val key: (InstanceData) -> T,
        val header: (T) -> String = { it.toString() },
        val comparator: Comparator<T> = Comparator.comparing { header(it) }
    ) {
        data object None : Behavior<String>(
            key = { "" }
        )

        data object GameVersion : Behavior<MojangVersionManifest.Version>(
            key = InstanceData::gameVersion,
            header = MojangVersionManifest.Version::id,
            comparator = Comparator.comparing(MojangVersionManifest.Version::releaseTime)
        )

        data object Loader : Behavior<ModLoader?>(
            key = InstanceData::loader,
            header = { it?.text ?: ModLoader.Vanilla }
        )
    }
}