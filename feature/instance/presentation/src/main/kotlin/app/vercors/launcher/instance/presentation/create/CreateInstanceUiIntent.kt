package app.vercors.launcher.instance.presentation.create

import app.vercors.launcher.game.domain.loader.ModLoaderType

sealed interface CreateInstanceUiIntent {
    @JvmInline
    value class UpdateInstanceName(val value: String) : CreateInstanceUiIntent

    @JvmInline
    value class SelectGameVersion(val value: String) : CreateInstanceUiIntent

    @JvmInline
    value class ToggleShowSnapshots(val value: Boolean) : CreateInstanceUiIntent

    @JvmInline
    value class SelectModLoader(val value: ModLoaderType?) : CreateInstanceUiIntent

    @JvmInline
    value class SelectModLoaderVersion(val value: String) : CreateInstanceUiIntent

    data object CreateInstance : CreateInstanceUiIntent

    data object CloseDialog : CreateInstanceUiIntent
}
