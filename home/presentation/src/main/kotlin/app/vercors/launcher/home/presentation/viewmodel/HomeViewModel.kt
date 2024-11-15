package app.vercors.launcher.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import app.vercors.launcher.core.presentation.viewmodel.MviViewModel
import app.vercors.launcher.home.domain.model.HomeSection
import app.vercors.launcher.home.domain.repository.HomeRepository
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val homeRepository: HomeRepository,
) : MviViewModel<HomeUiState, HomeUiIntent, HomeUiEffect>(HomeUiState()) {
    override fun onStart() {
        super.onStart()
        viewModelScope.launch {
            homeRepository.sectionsState.collect {
                onIntent(UpdateSections(it))
            }
        }
        viewModelScope.launch { homeRepository.loadSections() }
    }

    override fun HomeUiState.reduce(intent: HomeUiIntent): HomeUiState =
        when (intent) {
            HomeUiIntent.CreateInstance -> withEffect(HomeUiEffect.CreateInstance)
            is HomeUiIntent.InstallProject -> this
            is HomeUiIntent.LaunchOrStopInstance -> this
            is HomeUiIntent.ShowInstance -> withEffect(HomeUiEffect.NavigateToInstance(intent.instanceId))
            is HomeUiIntent.ShowProject -> withEffect(HomeUiEffect.NavigateToProject(intent.projectId))
            is UpdateSections -> HomeUiState(intent.sections.map { it.toUi() })
        }


    @JvmInline
    private value class UpdateSections(val sections: List<HomeSection>) : HomeUiIntent
}