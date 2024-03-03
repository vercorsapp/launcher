package app.vercors.instance.launch

import app.vercors.instance.InstanceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface LauncherService {
    fun launch(instance: InstanceData, context: CoroutineContext = Dispatchers.IO): Flow<LaunchStatus>
}