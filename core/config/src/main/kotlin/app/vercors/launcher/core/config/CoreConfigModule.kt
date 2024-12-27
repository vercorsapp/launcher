package app.vercors.launcher.core.config

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import app.vercors.launcher.core.config.proto.ConfigProto
import app.vercors.launcher.core.config.serializer.ConfigSerializer
import app.vercors.launcher.core.storage.Storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.io.File

@Module
@ComponentScan
class CoreConfigModule

@Single
fun provideConfigDataStore(
    storage: Storage,
    serializer: ConfigSerializer,
    externalScope: CoroutineScope,
    @Named("ioDispatcher") ioDispatcher: CoroutineDispatcher,
): DataStore<ConfigProto> = DataStoreFactory.create(
    serializer = serializer,
    produceFile = { File(storage.state.value.path, "config.pb") },
    scope = CoroutineScope(externalScope.coroutineContext + ioDispatcher)
)
