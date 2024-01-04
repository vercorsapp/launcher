package com.skyecodes.snowball.service

import com.skyecodes.snowball.APP_NAME
import net.harawata.appdirs.AppDirsFactory

object CacheService {
    val appDirs = AppDirsFactory.getInstance()
    val cacheDir = appDirs.getUserCacheDir(APP_NAME, null, null)

    fun validate() {

    }
}