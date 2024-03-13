package app.vercors.instance

sealed interface InstanceStatus {
    data object Stopped : InstanceStatus
    data class Launching(val progress: Float) : InstanceStatus
    data object Running : InstanceStatus
    data class Errored(val error: Throwable) : InstanceStatus
}
