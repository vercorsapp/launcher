package app.vercors.launcher.setup.presentation

import androidx.compose.runtime.Immutable
import kotlinx.io.files.Path

@Immutable
data class SetupUiState(
    val path: String
) {
    val parentPath: String = Path(path).parent.toString()
}