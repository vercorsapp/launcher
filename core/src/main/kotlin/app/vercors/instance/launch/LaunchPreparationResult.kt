package app.vercors.instance.launch

import app.vercors.instance.mojang.data.MojangVersionInfo
import java.nio.file.Path

data class LaunchPreparationResult(
    val versionInfo: MojangVersionInfo,
    val clientJarPath: String,
    val libraryPaths: List<String>,
    val logConfigPath: Path?
)
