package com.skyecodes.snowball

import com.skyecodes.snowball.service.CurseforgeApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class CurseforgeApiTest {
    @Test
    fun test() {
        runBlocking {
            val featuredMods = CurseforgeApi.getPopularMods()
            assertTrue(featuredMods.data.isNotEmpty())
            val classes = CurseforgeApi.getClasses()
            assertTrue(true)
        }
    }
}