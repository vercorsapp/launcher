package app.vercors.launcher.core.storage

import kotlinx.io.files.Path

data class StorageState(
    val strPath: String,
) {
    val path: Path = Path(strPath)
    val isSetup: Boolean = strPath != Storage.DEFAULT_PATH
}
