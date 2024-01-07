package com.skyecodes.snowball.ui.home

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.skyecodes.snowball.data.app.Mod
import com.skyecodes.snowball.data.app.fromCurseforge
import com.skyecodes.snowball.data.app.fromModrinth
import com.skyecodes.snowball.service.CurseforgeApi
import com.skyecodes.snowball.service.ModrinthApi
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Home
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
            val icon = rememberVectorPainter(FontAwesomeIcons.Solid.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        var featuredCurseforge: List<Mod> by rememberSaveable { mutableStateOf(emptyList()) }
        var popularModrinth: List<Mod> by rememberSaveable { mutableStateOf(emptyList()) }
        var popularCurseforge: List<Mod> by rememberSaveable { mutableStateOf(emptyList()) }
        var recentlyUpdatedModrinth: List<Mod> by rememberSaveable { mutableStateOf(emptyList()) }
        var recentlyUpdatedCurseforge: List<Mod> by rememberSaveable { mutableStateOf(emptyList()) }
        val scope = rememberCoroutineScope()

        if (featuredCurseforge.isEmpty() || popularCurseforge.isEmpty() || recentlyUpdatedCurseforge.isEmpty()) {
            scope.launch {
                try {
                    val apiData = CurseforgeApi.getFeaturedMods(emptyList()).data
                    featuredCurseforge = apiData.featured.fromCurseforge()
                    popularCurseforge = apiData.popular.fromCurseforge()
                    recentlyUpdatedCurseforge = apiData.recentlyUpdated.fromCurseforge()
                } catch (e: CancellationException) {
                    // do nothing
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        if (popularModrinth.isEmpty()) {
            scope.launch {
                try {
                    popularModrinth = ModrinthApi.getPopularMods().hits.fromModrinth()
                    println("a")
                } catch (e: CancellationException) {
                    // do nothing
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        if (recentlyUpdatedModrinth.isEmpty()) {
            scope.launch {
                try {
                    recentlyUpdatedModrinth = ModrinthApi.getRecentlyUpdatedMods().hits.fromModrinth()
                } catch (e: CancellationException) {
                    // do nothing
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        HomeContent(featuredCurseforge, popularModrinth, popularCurseforge, recentlyUpdatedModrinth, recentlyUpdatedCurseforge)

    }
}

@Composable
private fun HomeContent(
    featuredCurseforge: List<Mod>,
    popularModrinth: List<Mod>,
    popularCurseforge: List<Mod>,
    recentlyUpdatedModrinth: List<Mod>,
    recentlyUpdatedCurseforge: List<Mod>
) {
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