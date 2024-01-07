package com.skyecodes.snowball.ui.accounts

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.User

object AccountsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Accounts"
            val icon = rememberVectorPainter(FontAwesomeIcons.Solid.User)

            return remember {
                TabOptions(
                    index = 3u,
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