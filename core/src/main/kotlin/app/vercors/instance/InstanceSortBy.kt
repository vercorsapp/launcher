package app.vercors.instance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class InstanceSortBy(val comparator: Comparator<InstanceData>) {
    @SerialName("lastPlayed")
    LastPlayed(Comparator.comparing { it.lastPlayed ?: it.dateCreated }),

    @SerialName("dateModified")
    DateModified(Comparator.comparing(InstanceData::dateModified)),

    @SerialName("dateCreated")
    DateCreated(Comparator.comparing(InstanceData::dateCreated)),

    @SerialName("gameVersion")
    GameVersion(Comparator.comparing { it.gameVersion.releaseTime }),

    @SerialName("name")
    Name(Comparator.comparing(InstanceData::name))
}