package app.vercors.launcher.instance.presentation.create

import androidx.compose.runtime.Immutable
import app.vercors.launcher.game.domain.loader.ModLoaderType

@Immutable
data class CreateInstanceUiState(
    val instanceName: String = "",
    val showSnapshots: Boolean = false,
    val selectedGameVersionId: String? = null,
    val gameVersions: List<GameVersionUi>? = null,
    val modLoader: ModLoaderType? = null,
    val modLoaderVersions: List<String>? = null,
    val selectedModLoaderVersion: String? = null,
) {
    val filteredGameVersions: List<GameVersionUi>? =
        if (showSnapshots) gameVersions else gameVersions?.filter { !it.isSnapshot }
    val selectedGameVersion: GameVersionUi? = gameVersions?.find { it.id == selectedGameVersionId }
    val isValid =
        instanceName.isNotBlank() && selectedGameVersionId != null && (modLoader == null || selectedModLoaderVersion != null)
}

data class GameVersionUi(
    val id: String,
    val isSnapshot: Boolean,
    val isLatestRelease: Boolean,
    val isLatestSnapshot: Boolean,
)

operator fun List<GameVersionUi>.get(id: String) = first { it.id == id }
