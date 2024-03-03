package app.vercors.instance

import java.nio.file.Path


interface InstanceLoadingState {
    data object NotLoaded : InstanceLoadingState
    data class Loading(
        val instances: List<InstanceData> = emptyList(),
        val warns: List<Path> = emptyList(),
        val errors: List<Throwable> = emptyList()
    ) : InstanceLoadingState

    data class Loaded(
        val instances: List<InstanceData>,
        val warns: List<Path>,
        val errors: List<Throwable>
    ) : InstanceLoadingState
}
