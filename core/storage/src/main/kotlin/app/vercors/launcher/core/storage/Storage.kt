package app.vercors.launcher.core.storage

import kotlinx.coroutines.flow.StateFlow

interface Storage {
    val state: StateFlow<StorageState>
    fun updatePath(path: String)

    companion object {
        val instance by lazy { StorageImpl() }
        const val DEFAULT_PATH = "."
    }
}