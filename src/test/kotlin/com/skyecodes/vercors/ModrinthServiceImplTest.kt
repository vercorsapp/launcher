package com.skyecodes.vercors

import com.skyecodes.vercors.data.service.HttpServiceImpl
import com.skyecodes.vercors.data.service.ModrinthServiceImpl
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ModrinthServiceImplTest {
    @Test
    fun test() {
        runBlocking {
            val modrinthServiceImpl = ModrinthServiceImpl(HttpServiceImpl())
            val popularMods = modrinthServiceImpl.getPopularMods()
            assertTrue(popularMods.hits.isNotEmpty())
        }
    }
}