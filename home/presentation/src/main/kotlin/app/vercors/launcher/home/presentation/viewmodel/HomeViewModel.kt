package app.vercors.launcher.home.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.vercors.launcher.core.generated.resources.hard_drive
import app.vercors.launcher.core.presentation.navigation.AppDestination
import app.vercors.launcher.core.presentation.navigation.Navigator
import app.vercors.launcher.home.domain.usecase.ObserveHomeSections
import app.vercors.launcher.home.generated.resources.jump_back_in
import app.vercors.launcher.home.presentation.action.HomeAction
import app.vercors.launcher.home.presentation.model.HomeSectionItemUi
import app.vercors.launcher.home.presentation.model.HomeSectionUi
import app.vercors.launcher.home.presentation.state.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import app.vercors.launcher.core.generated.resources.Res as CoreRes
import app.vercors.launcher.home.generated.resources.Res as HomeRes

@KoinViewModel
class HomeViewModel(
    private val navigator: Navigator,
    private val observeHomeSections: ObserveHomeSections
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(HomeState(false, listOf(HomeSectionUi(HomeRes.string.jump_back_in, buildList {
            repeat(10) {
                add(
                    HomeSectionItemUi.Instance(
                        name = "mau $it",
                        loader = "mauloader",
                        gameVersion = "69.420",
                        status = "meowing",
                        statusIcon = CoreRes.drawable.hard_drive,
                        statusColor = Color.Unspecified
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
            is HomeAction.InstallProject -> TODO()
            is HomeAction.LaunchInstance -> TODO()
            is HomeAction.ShowInstance -> viewModelScope.launch {
                navigator.navigateTo(AppDestination.InstanceDetails(action.instanceId))
            }

            is HomeAction.ShowProject -> viewModelScope.launch {
                navigator.navigateTo(AppDestination.ProjectDetails(action.provider, action.projectId))
            }
        }
    }
}