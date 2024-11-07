package app.vercors.launcher.core.storage

import androidx.compose.runtime.Immutable

@Immutable
data class StorageState(
    val path: String,
) {
    val isSetup: Boolean = path != Storage.DEFAULT_PATH
}
