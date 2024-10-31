package app.vercors.launcher.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import app.vercors.launcher.home.domain.usecase.ObserveHomeSections
import app.vercors.launcher.home.generated.resources.jump_back_in
import app.vercors.launcher.home.presentation.HomeString
import app.vercors.launcher.home.presentation.action.HomeAction
import app.vercors.launcher.home.presentation.model.HomeInstanceStatusUi
import app.vercors.launcher.home.presentation.model.HomeSectionItemUi
import app.vercors.launcher.home.presentation.model.HomeSectionUi
import app.vercors.launcher.home.presentation.state.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val observeHomeSections: ObserveHomeSections
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(HomeState(false, listOf(HomeSectionUi(HomeString.jump_back_in, buildList {
            repeat(10) {
                add(
                    HomeSectionItemUi.Instance(
                        name = "mau $it",
                        loader = "mauloader",
                        gameVersion = "69.420",
                        status = HomeInstanceStatusUi.Running
                    )
                )
            }
        }))))
    val uiState = _uiState.asStateFlow()
    /*val uiState = observeHomeSections()
        .map { sections -> HomeState(isLoading = false, sections = sections.map { it.toUi() }) }
        .stateIn(viewModelScope, SharingStarted.Lazily, HomeState(isLoading = true))*/

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.Nav -> {}
        }
    }
}