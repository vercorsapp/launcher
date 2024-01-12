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
import com.skyecodes.snowball.service.CurseforgeService
import com.skyecodes.snowball.service.ModrinthService
import com.skyecodes.snowball.ui.UI
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home
import org.kodein.di.compose.rememberDI
import org.kodein.di.instance

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
            val icon = rememberVectorPainter(FeatherIcons.Home)

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
        val modrinthService: ModrinthService by rememberDI { instance() }
        val curseforgeService: CurseforgeService by rememberDI { instance() }

        Box(Modifier.padding(start = UI.mediumPadding, end = UI.smallPadding)) {
            val scrollState = rememberScrollState()

            Column(
                Modifier.fillMaxSize().padding(top = UI.smallPadding, bottom = UI.smallPadding, end = 18.dp)
                    .verticalScroll(scrollState)
            ) {
                HomeSection(
                    "Popular Mods",
                    { modrinthService.getPopularMods().hits.convertModrinth() },
                    { curseforgeService.getPopularMods().data.convertCurseforge() })
                HomeSection(
                    "Popular Modpacks",
                    { modrinthService.getPopularModpacks().hits.convertModrinth() },
                    { curseforgeService.getPopularModpacks().data.convertCurseforge() })
                HomeSection(
                    "Popular Resource Packs",
                    { modrinthService.getPopularResourcePacks().hits.convertModrinth() },
                    { curseforgeService.getPopularResourcePacks().data.convertCurseforge() })
                HomeSection(
                    "Popular Shader Packs",
                    { modrinthService.getPopularShaderPacks().hits.convertModrinth() },
                    { curseforgeService.getPopularShaderPacks().data.convertCurseforge() })
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState)
            )
        }
    }
}