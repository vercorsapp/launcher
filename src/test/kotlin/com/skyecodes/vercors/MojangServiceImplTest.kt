package com.skyecodes.vercors

import com.skyecodes.vercors.data.service.HttpServiceImpl
import com.skyecodes.vercors.data.service.MojangServiceImpl
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MojangServiceImplTest {
    @Test
    fun test() {
        runBlocking {
            val mojangServiceImpl = MojangServiceImpl(HttpServiceImpl())
            val manifest = mojangServiceImpl.getVersionManifest()
            assertTrue(manifest.versions.isNotEmpty())
            val latestRelease = manifest.latest.release
            val latestReleaseUrl = manifest.versions[latestRelease].url
            val latestReleaseInfo = mojangServiceImpl.getVersionInfo(latestReleaseUrl)
            assertEquals(latestReleaseInfo.id, manifest.latest.release)
            assertEquals(latestReleaseInfo.releaseTime, manifest.versions[latestRelease].releaseTime)
        }
    }
}