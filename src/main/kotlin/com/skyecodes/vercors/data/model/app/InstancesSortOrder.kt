package com.skyecodes.vercors.data.model.app

import com.skyecodes.vercors.ui.UI
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class InstancesSortOrder(val localizedText: (UI.Localization) -> String) {
    @SerialName("lastPlayed")
    LastPlayed(UI.Localization::lastPlayed),

    @SerialName("dateModified")
    DateModified(UI.Localization::dateModified),

    @SerialName("dateCreated")
    DateCreated(UI.Localization::dateCreated),

    @SerialName("gameVersion")
    GameVersion(UI.Localization::gameVersion),

    @SerialName("name")
    Name(UI.Localization::name),
}
