package app.vercors.configuration

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

interface ConfigurationService {
    val loadingState: StateFlow<ConfigurationLoadingState>
    val configState: StateFlow<ConfigurationData?>
    val config: ConfigurationData
    fun load(): Job
    fun update(config: ConfigurationData)
}