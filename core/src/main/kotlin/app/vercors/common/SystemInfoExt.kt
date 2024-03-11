package app.vercors.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oshi.SystemInfo
import kotlin.coroutines.CoroutineContext

suspend fun SystemInfo.totalMemory(context: CoroutineContext = Dispatchers.Default) = withContext(context) {
    hardware.memory.total / 1_000_000 / 1.024
}.toInt()