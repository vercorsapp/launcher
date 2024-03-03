package app.vercors.instance

import java.nio.file.Path

sealed interface InstanceLoadingResult {
    data class Success(val instance: InstanceData) : InstanceLoadingResult
    data class Warn(val path: Path) : InstanceLoadingResult
    data class Error(val error: Throwable) : InstanceLoadingResult
}