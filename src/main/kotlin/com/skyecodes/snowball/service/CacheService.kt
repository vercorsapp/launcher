package com.skyecodes.snowball.service

import com.skyecodes.snowball.APP_NAME
import net.harawata.appdirs.AppDirsFactory
import java.nio.file.Paths

object CacheService {
    private val appDirs = AppDirsFactory.getInstance()
    val cacheDir = Paths.get(appDirs.getUserCacheDir(APP_NAME, null, null))

    fun validate() {

    }
}