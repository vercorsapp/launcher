package com.skyecodes.vercors

import com.skyecodes.vercors.service.impl.HttpServiceImpl
import com.skyecodes.vercors.service.impl.ModrinthServiceImpl
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