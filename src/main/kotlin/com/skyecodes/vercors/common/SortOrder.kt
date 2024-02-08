package com.skyecodes.vercors.common

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ArrowUp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SortOrder(val value: String, val localizedName: (Localization) -> String, val icon: ImageVector) {
    @SerialName("asc")
    Asc("asc", Localization::ascending, FeatherIcons.ArrowUp),

    @SerialName("desc")
    Desc("desc", Localization::descending, FeatherIcons.ArrowDown);

    val opposite by lazy {
        when (this) {
            Asc -> Desc
            Desc -> Asc
        }
    }
}
