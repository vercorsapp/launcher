package app.vercors.launcher.app.setup

import kotlinx.io.files.Path

data class SetupUiState(
    val path: String
) {
    val parentPath: String = Path(path).parent.toString()
}
