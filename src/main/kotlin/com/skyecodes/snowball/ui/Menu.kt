package com.skyecodes.snowball.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import com.skyecodes.snowball.ui.accounts.AccountsTab
import com.skyecodes.snowball.ui.home.HomeTab
import com.skyecodes.snowball.ui.instances.InstancesTab
import com.skyecodes.snowball.ui.search.SearchTab
import com.skyecodes.snowball.ui.settings.SettingsTab

@Composable
fun Menu() {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TabNavigationItem(HomeTab)
        TabNavigationItem(InstancesTab)
        TabNavigationItem(SearchTab)
        Spacer(Modifier.weight(1f))
        TabNavigationItem(AccountsTab)
        TabNavigationItem(SettingsTab)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    TooltipArea(
        tooltip = { TabTooltip(tab) },
        tooltipPlacement = TooltipPlacement.ComponentRect(
            alignment = Alignment.CenterEnd,
            offset = DpOffset(30.dp, -(25.dp))
        ),
        delayMillis = 250,
    ) {
        Button(
            modifier = Modifier.size(50.dp),
            elevation = if (tabNavigator.current === tab) ButtonDefaults.elevation() else null,
            colors = if (tabNavigator.current === tab) ButtonDefaults.buttonColors(backgroundColor = UI.colors.surface1)
            else ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onSurface),
            contentPadding = PaddingValues(12.dp),
            onClick = { tabNavigator.current = tab }
        ) {
            Icon(tab.options.icon!!, tab.options.title, Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun TabTooltip(tab: Tab) {
    Surface(
        color = UI.colors.surface1,
        modifier = Modifier.shadow(4.dp),
        shape = UI.defaultCornerShape
    ) {
        Text(
            style = MaterialTheme.typography.body1,
            text = tab.options.title,
            modifier = Modifier.padding(horizontal = UI.mediumPadding, vertical = UI.smallPadding),
        )
    }
}