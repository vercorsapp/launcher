package app.vercors.launcher.instance.presentation.create

sealed interface CreateInstanceUiIntent {
    @JvmInline
    value class UpdateInstanceName(val value: String) : CreateInstanceUiIntent

    data object CreateInstance : CreateInstanceUiIntent

    data object CloseDialog : CreateInstanceUiIntent
}
