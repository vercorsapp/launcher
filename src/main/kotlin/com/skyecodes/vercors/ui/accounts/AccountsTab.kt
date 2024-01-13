package com.skyecodes.vercors.ui.accounts

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import compose.icons.FeatherIcons
import compose.icons.feathericons.User

object AccountsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Accounts"
            val icon = rememberVectorPainter(FeatherIcons.User)

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