package com.skyecodes.vercors.ui.search

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search

object SearchTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Search"
            val icon = rememberVectorPainter(FeatherIcons.Search)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Text(options.title)
    }
}