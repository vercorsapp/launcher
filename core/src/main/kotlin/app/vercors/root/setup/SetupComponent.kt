package app.vercors.root.setup

import app.vercors.root.RootChildComponent
import kotlinx.coroutines.flow.StateFlow

interface SetupComponent : RootChildComponent {
    val uiState: StateFlow<SetupUiState>
    fun updatePath(path: String)
    fun openDirectoryPicker()
    fun closeDirectoryPicker()
    fun updateShowTutorial(showTutorial: Boolean)
    fun launch()
}