package app.vercors.instance.launch

import app.vercors.instance.InstanceData
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface LauncherService {
    fun launchInstance(instance: InstanceData, context: CoroutineContext = Dispatchers.IO)
}