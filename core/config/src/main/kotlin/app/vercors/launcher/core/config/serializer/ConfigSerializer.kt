package app.vercors.launcher.core.config.serializer

import androidx.datastore.core.Serializer
import app.vercors.launcher.core.config.mapper.toProto
import app.vercors.launcher.core.config.model.AppConfig
import app.vercors.launcher.core.config.proto.ConfigProto
import org.koin.core.annotation.Single
import java.io.InputStream
import java.io.OutputStream

@Single
class ConfigSerializer : Serializer<ConfigProto> {
    override val defaultValue: ConfigProto = AppConfig.DEFAULT.toProto()
    override suspend fun readFrom(input: InputStream): ConfigProto = ConfigProto.parseFrom(input)
    override suspend fun writeTo(t: ConfigProto, output: OutputStream) = t.writeTo(output)
}