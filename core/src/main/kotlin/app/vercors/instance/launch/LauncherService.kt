package app.vercors.instance.launch

import app.vercors.instance.InstanceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface LauncherService {
    fun prepareInstance(instance: InstanceData, context: CoroutineContext = Dispatchers.IO): Job
    fun launchInstance(instance: InstanceData, context: CoroutineContext = Dispatchers.IO): Job
}