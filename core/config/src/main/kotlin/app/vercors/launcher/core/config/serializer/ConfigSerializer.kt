package app.vercors.launcher.core.config.serializer

import androidx.datastore.core.Serializer
import app.vercors.launcher.core.config.proto.Config
import org.koin.core.annotation.Single
import java.io.InputStream
import java.io.OutputStream

@Single
class ConfigSerializer : Serializer<Config> {
    override val defaultValue: Config = Config.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Config = Config.parseFrom(input)
    override suspend fun writeTo(t: Config, output: OutputStream) = t.writeTo(output)
}