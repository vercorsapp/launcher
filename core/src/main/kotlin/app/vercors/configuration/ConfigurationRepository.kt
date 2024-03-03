package app.vercors.configuration

interface ConfigurationRepository {
    val defaultConfig: ConfigurationData
    suspend fun load(): ConfigurationData
    suspend fun save(config: ConfigurationData)
}