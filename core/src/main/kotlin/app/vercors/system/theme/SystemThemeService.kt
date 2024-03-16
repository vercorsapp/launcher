package app.vercors.system.theme

import kotlinx.coroutines.flow.StateFlow

interface SystemThemeService {
    val isDark: StateFlow<Boolean>
    suspend fun awaitInit()
}