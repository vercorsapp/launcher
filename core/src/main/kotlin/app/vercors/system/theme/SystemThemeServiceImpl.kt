package app.vercors.system.theme

import com.jthemedetecor.OsThemeDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SystemThemeServiceImpl(
    coroutineScope: CoroutineScope,
    osThemeDetectorProvider: () -> OsThemeDetector = { OsThemeDetector.getDetector() }
) : SystemThemeService, CoroutineScope by coroutineScope {
    private lateinit var osThemeDetector: OsThemeDetector
    private val _isDark = MutableStateFlow(true)
    override val isDark: StateFlow<Boolean> = _isDark
    private val initJob = launch {
        osThemeDetector = osThemeDetectorProvider()
        _isDark.update { osThemeDetector.isDark }
        osThemeDetector.registerListener { _isDark.update { _ -> it } }
    }

    override suspend fun awaitInit() = initJob.join()
}