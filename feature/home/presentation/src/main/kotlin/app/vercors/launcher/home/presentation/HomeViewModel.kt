package app.vercors.launcher.home.presentation

import app.vercors.launcher.core.presentation.mvi.MviViewModel
import app.vercors.launcher.home.domain.HomeRepository
import app.vercors.launcher.home.domain.HomeSection
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val homeRepository: HomeRepository,
) : MviViewModel<HomeUiState, HomeUiIntent, HomeUiEffect>(HomeUiState()) {
    override fun onStart() {
        super.onStart()
        collectInScope(homeRepository.sectionsState) {
            onIntent(UpdateSections(it))
        }
        runInScope { homeRepository.loadSections() }
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