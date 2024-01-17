package com.skyecodes.vercors.data.service

import com.skyecodes.vercors.APP_NAME
import net.harawata.appdirs.AppDirsFactory
import java.nio.file.Path
import java.nio.file.Paths

interface StorageService {
    val cacheDir: Path
    val dataDir: Path
    val configDir: Path
    val instancesDir: Path
}

class StorageServiceImpl : StorageService {
    private val appDirs = AppDirsFactory.getInstance()
    override val cacheDir: Path = Paths.get(appDirs.getUserCacheDir(APP_NAME, null, null))
    override val dataDir: Path = Paths.get(appDirs.getUserDataDir(APP_NAME, null, null))
    override val configDir: Path = Paths.get(appDirs.getUserConfigDir(APP_NAME, null, null))
    override val instancesDir: Path = dataDir.resolve("instances")
}