package app.vercors.launcher.core.data.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.koin.core.annotation.Single

@Single
fun provideJson() = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
    serializersModule = SerializersModule {
        contextual(DurationSerializer)
    }
}