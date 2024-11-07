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
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

private val logger = KotlinLogging.logger {}

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

    fun onAction(action: HomeAction) {
        logger.debug { "Action triggered in HomeViewModel: $action" }
    }
}