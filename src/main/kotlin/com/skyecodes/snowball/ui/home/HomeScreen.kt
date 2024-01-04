package com.skyecodes.snowball.ui.home

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.skyecodes.snowball.data.app.Mod
import com.skyecodes.snowball.data.app.fromCurseforge
import com.skyecodes.snowball.data.app.fromModrinth
import com.skyecodes.snowball.service.CurseforgeApi
import com.skyecodes.snowball.service.ModrinthApi
import kotlinx.coroutines.launch

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        var featuredCurseforge: List<Mod> by remember { mutableStateOf(emptyList()) }
        var popularModrinth: List<Mod> by remember { mutableStateOf(emptyList()) }
        var popularCurseforge: List<Mod> by remember { mutableStateOf(emptyList()) }
        var recentlyUpdatedModrinth: List<Mod> by remember { mutableStateOf(emptyList()) }
        var recentlyUpdatedCurseforge: List<Mod> by remember { mutableStateOf(emptyList()) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(this) {
            scope.launch {
                val apiData = CurseforgeApi.getFeaturedMods(emptyList()).data
                featuredCurseforge = apiData.featured.fromCurseforge()
                popularCurseforge = apiData.popular.fromCurseforge()
                recentlyUpdatedCurseforge = apiData.recentlyUpdated.fromCurseforge()
            }
            scope.launch {
                popularModrinth = ModrinthApi.getPopularMods().hits.fromModrinth()
            }
            scope.launch {
                recentlyUpdatedModrinth = ModrinthApi.getRecentlyUpdatedMods().hits.fromModrinth()
            }
        }

        Box {
            val scrollState = rememberScrollState()

            Column(Modifier.fillMaxSize().padding(end = 10.dp).verticalScroll(scrollState)) {
                HomeSection("Featured on CurseForge", featuredCurseforge)
                HomeSection("Popular on Modrinth", popularModrinth)
                HomeSection("Popular on CurseForge", popularCurseforge)
                HomeSection("Recently updated on Modrinth", recentlyUpdatedModrinth)
                HomeSection("Recently updated on CurseForge", recentlyUpdatedCurseforge)
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState)
            )
        }

    }
}