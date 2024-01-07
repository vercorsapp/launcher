package com.skyecodes.snowball.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        TabNavigationItem(HomeTab, true)
        TabNavigationItem(InstancesTab)
        TabNavigationItem(SearchTab)
        Spacer(Modifier.weight(1f))
        TabNavigationItem(AccountsTab)
        TabNavigationItem(SettingsTab)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabNavigationItem(tab: Tab, isFirst: Boolean = false) {
    val tabNavigator = LocalTabNavigator.current

    TooltipArea(
        tooltip = { TabTooltip(tab) },
        tooltipPlacement = if (isFirst) TooltipPlacement.ComponentRect(
            alignment = Alignment.CenterEnd,
            offset = DpOffset(35.dp, -(25.dp))
        ) else TooltipPlacement.ComponentRect(
            alignment = Alignment.TopCenter,
            offset = DpOffset(0.dp, -(60.dp))
        ),
        delayMillis = 250,
    ) {
        if (tabNavigator.current === tab) {
            Button(
                modifier = Modifier.size(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Theme.currentPalette.surface1),
                contentPadding = PaddingValues(12.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = { tabNavigator.current = tab }
            ) {
                Icon(tab.options.icon!!, tab.options.title, Modifier.size(30.dp))
            }
        } else {
            TextButton(
                modifier = Modifier.size(50.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onSurface),
                contentPadding = PaddingValues(12.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = { tabNavigator.current = tab }
            ) {
                Icon(tab.options.icon!!, tab.options.title, Modifier.size(30.dp))
            }
        }
    }
}

@Composable
private fun TabTooltip(tab: Tab) {
    Surface(
        color = Theme.currentPalette.surface1,
        modifier = Modifier.shadow(4.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = tab.options.title,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontSize = 14.sp
        )
    }
}