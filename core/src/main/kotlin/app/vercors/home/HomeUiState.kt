package app.vercors.home

import kotlinx.collections.immutable.ImmutableList

data class HomeUiState(
    val sections: ImmutableList<HomeSection>
)
