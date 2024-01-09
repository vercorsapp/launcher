package com.skyecodes.snowball.service

import com.skyecodes.snowball.APP_NAME
import net.harawata.appdirs.AppDirsFactory
import java.nio.file.Path
import java.nio.file.Paths

object StorageService {
    private val appDirs = AppDirsFactory.getInstance()
    val cacheDir: Path = Paths.get(appDirs.getUserCacheDir(APP_NAME, null, null))
    val dataDir: Path = Paths.get(appDirs.getUserDataDir(APP_NAME, null, null))
    val configDir: Path = Paths.get(appDirs.getUserConfigDir(APP_NAME, null, null))
    val instancesDir: Path = dataDir.resolve("instances")
}