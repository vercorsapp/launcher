package app.vercors.launcher.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import app.vercors.launcher.core.presentation.viewmodel.MviViewModel
import app.vercors.launcher.core.presentation.viewmodel.StateResult
import app.vercors.launcher.home.domain.repository.HomeRepository
import app.vercors.launcher.home.presentation.mapper.toUi
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val homeRepository: HomeRepository,
) : MviViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>(HomeUiState()) {
    override fun init() {
        viewModelScope.launch {
            homeRepository.sectionsState.collect {
                onEvent(HomeUiEvent.UpdateSections(it))
            }
        }
        viewModelScope.launch { homeRepository.loadSections() }
    }

    override fun reduce(state: HomeUiState, event: HomeUiEvent): StateResult<HomeUiState, HomeUiEffect> =
        when (event) {
            HomeUiIntent.CreateInstance -> StateResult.Unchanged(HomeUiEffect.OpenCreateInstanceDialog)
            is HomeUiIntent.InstallProject -> StateResult.Unchanged()
            is HomeUiIntent.LaunchOrStopInstance -> StateResult.Unchanged()
            is HomeUiIntent.ShowInstance -> StateResult.Unchanged(HomeUiEffect.NavigateToInstance(event.instanceId))
            is HomeUiIntent.ShowProject -> StateResult.Unchanged(HomeUiEffect.NavigateToProject(event.projectId))
            is HomeUiEvent.UpdateSections -> StateResult.Changed(HomeUiState(event.sections.map { it.toUi() }))
        }
}