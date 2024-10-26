package app.vercors.launcher.home.presentation.model

import org.jetbrains.compose.resources.StringResource

data class HomeSectionUi(
    val title: StringResource,
    val items: List<HomeSectionItemUi>
)