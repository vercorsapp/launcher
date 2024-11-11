package app.vercors.launcher.home.presentation.viewmodel

import app.vercors.launcher.home.domain.model.HomeSection

sealed interface HomeUiEvent {
    @JvmInline
    value class UpdateSections(val sections: List<HomeSection>) : HomeUiEvent
}