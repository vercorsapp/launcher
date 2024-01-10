package com.skyecodes.snowball.service

import java.nio.file.Path

interface StorageService {
    val cacheDir: Path
    val dataDir: Path
    val configDir: Path
    val instancesDir: Path
}