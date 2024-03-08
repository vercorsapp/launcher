package app.vercors.dialog

import kotlinx.serialization.Serializable

@Serializable
sealed interface DialogConfig {
    @Serializable
    data object CreateInstance : DialogConfig

    @Serializable
    data class Login(val authenticationStateCollector: () -> Unit = {}) : DialogConfig

    @Serializable
    data class Error(val title: String, val message: List<String>) : DialogConfig
}