package com.skyecodes.vercors.settings

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ConfigurationService {
    val state: StateFlow<ConfigurationState>
    val config: Flow<Configuration>
    fun loadConfiguration(): Job
    fun updateConfiguration(config: Configuration)
}

