package com.skyecodes.vercors.service

import com.skyecodes.vercors.data.app.Configuration

interface ConfigurationService {
    suspend fun load(): Configuration
    suspend fun save(config: Configuration)
}