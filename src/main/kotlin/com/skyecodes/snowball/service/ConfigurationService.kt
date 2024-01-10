package com.skyecodes.snowball.service

import com.skyecodes.snowball.data.app.Configuration

interface ConfigurationService {
    suspend fun load(): Configuration
    suspend fun save(config: Configuration)
}