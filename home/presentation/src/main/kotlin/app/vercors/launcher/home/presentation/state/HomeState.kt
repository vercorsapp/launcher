package app.vercors.launcher.home.presentation.state

import androidx.compose.runtime.Immutable
import app.vercors.launcher.home.presentation.model.HomeSectionUi

@Immutable
data class HomeState(
    val isLoading: Boolean = false,
    val sections: List<HomeSectionUi> = emptyList(),
)
