package app.vercors.dialog

import app.vercors.instance.InstanceData
import kotlinx.serialization.Serializable

@Serializable
sealed interface DialogConfig {
    @Serializable
    data object CreateInstance : DialogConfig

    @Serializable
    data class Login(val authenticationStateCollector: () -> Unit = {}) : DialogConfig

    @Serializable
    sealed interface Error : DialogConfig {
        @Serializable
        data object Launch : Error

        @Serializable
        data class JavaVersion(val instance: InstanceData, val javaVersion: Int) : Error
    }
}