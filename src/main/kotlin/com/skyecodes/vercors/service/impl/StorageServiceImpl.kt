package com.skyecodes.vercors.service.impl

import com.skyecodes.vercors.APP_NAME
import com.skyecodes.vercors.service.StorageService
import net.harawata.appdirs.AppDirsFactory
import java.nio.file.Path
import java.nio.file.Paths

class StorageServiceImpl : StorageService {
    private val appDirs = AppDirsFactory.getInstance()
    override val cacheDir: Path = Paths.get(appDirs.getUserCacheDir(APP_NAME, null, null))
    override val dataDir: Path = Paths.get(appDirs.getUserDataDir(APP_NAME, null, null))
    override val configDir: Path = Paths.get(appDirs.getUserConfigDir(APP_NAME, null, null))
    override val instancesDir: Path = dataDir.resolve("instances")
}