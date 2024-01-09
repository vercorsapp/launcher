package com.skyecodes.snowball.ui.home

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.skyecodes.snowball.data.app.convertCurseforge
import com.skyecodes.snowball.data.app.convertModrinth
import com.skyecodes.snowball.service.CurseforgeApi
import com.skyecodes.snowball.service.ModrinthApi
import com.skyecodes.snowball.ui.UI
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Home

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
        Box(Modifier.padding(start = UI.mediumPadding, end = UI.smallPadding)) {
            val scrollState = rememberScrollState()

            Column(
                Modifier.fillMaxSize().padding(top = UI.smallPadding, bottom = UI.smallPadding, end = 18.dp)
                    .verticalScroll(scrollState)
            ) {
                HomeSection(
                    "Popular Mods",
                    { ModrinthApi.getPopularMods().hits.convertModrinth() },
                    { CurseforgeApi.getPopularMods().data.convertCurseforge() })
                HomeSection(
                    "Popular Modpacks",
                    { ModrinthApi.getPopularModpacks().hits.convertModrinth() },
                    { CurseforgeApi.getPopularModpacks().data.convertCurseforge() })
                HomeSection(
                    "Popular Resource Packs",
                    { ModrinthApi.getPopularResourcePacks().hits.convertModrinth() },
                    { CurseforgeApi.getPopularResourcePacks().data.convertCurseforge() })
                HomeSection(
                    "Popular Shader Packs",
                    { ModrinthApi.getPopularShaderPacks().hits.convertModrinth() },
                    { CurseforgeApi.getPopularShaderPacks().data.convertCurseforge() })
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState)
            )
        }
    }
}