package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.ui.Localization
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class InstanceSortBy(val localizedName: (Localization) -> String, val comparator: Comparator<Instance>) {
    @SerialName("lastPlayed")
    LastPlayed(Localization::lastPlayed, Comparator.comparing { it.lastPlayed ?: it.dateCreated }),

    @SerialName("dateModified")
    DateModified(Localization::dateModified, Comparator.comparing(Instance::dateModified)),

    @SerialName("dateCreated")
    DateCreated(Localization::dateCreated, Comparator.comparing(Instance::dateCreated)),

    @SerialName("gameVersion")
    GameVersion(Localization::gameVersion, Comparator.comparing { it.gameVersion.releaseTime }),

    @SerialName("name")
    Name(Localization::name, Comparator.comparing(Instance::name))
}