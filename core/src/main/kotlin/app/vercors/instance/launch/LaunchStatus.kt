package app.vercors.instance.launch

import kotlinx.coroutines.CompletableJob

sealed interface LaunchStatus {
    data class RequiresLogin(val job: CompletableJob) : LaunchStatus
    data class Errored(val error: Throwable) : LaunchStatus
    data class Preparing(val progress: Float) : LaunchStatus
    data object Running : LaunchStatus
    data object Stopped : LaunchStatus
}