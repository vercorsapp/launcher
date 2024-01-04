package com.skyecodes.snowball

import com.skyecodes.snowball.service.ModrinthApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ModrinthApiTest {
    @Test
    fun test() {
        runBlocking {
            val popularMods = ModrinthApi.getPopularMods()
            assertTrue(popularMods.hits.isNotEmpty())
        }
    }
}