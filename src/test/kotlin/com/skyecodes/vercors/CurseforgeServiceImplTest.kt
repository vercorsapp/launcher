package com.skyecodes.vercors

import com.skyecodes.vercors.data.service.CurseforgeServiceImpl
import com.skyecodes.vercors.data.service.HttpServiceImpl
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class CurseforgeServiceImplTest {
    @Test
    fun test() {
        runBlocking {
            val curseforgeServiceImpl = CurseforgeServiceImpl(HttpServiceImpl())
            val featuredMods = curseforgeServiceImpl.getPopularMods()
            assertTrue(featuredMods.data.isNotEmpty())
            val classes = curseforgeServiceImpl.getClasses()
            assertTrue(true)
        }
    }
}