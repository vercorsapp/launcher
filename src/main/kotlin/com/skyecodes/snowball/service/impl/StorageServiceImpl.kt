package com.skyecodes.snowball.service.impl

import com.skyecodes.snowball.APP_NAME
import com.skyecodes.snowball.service.StorageService
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