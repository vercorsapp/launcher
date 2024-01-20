package com.skyecodes.vercors.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.data.model.app.AppTab

@Composable
fun Menu(currentTab: AppTab, onNavigate: (AppTab) -> Unit) {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TabNavigationItem(AppTab.Home, currentTab, onNavigate)
        TabNavigationItem(AppTab.Instances, currentTab, onNavigate)
        TabNavigationItem(AppTab.Search, currentTab, onNavigate)
        Spacer(Modifier.weight(1f))
        TabNavigationItem(AppTab.Accounts, currentTab, onNavigate)
        TabNavigationItem(AppTab.Settings, currentTab, onNavigate)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabNavigationItem(tab: AppTab, currentTab: AppTab, onNavigate: (AppTab) -> Unit) {
    TooltipArea(
        tooltip = { TabTooltip(tab.title) },
        tooltipPlacement = TooltipPlacement.ComponentRect(
            alignment = Alignment.CenterEnd,
            offset = DpOffset(30.dp, -(25.dp))
        ),
        delayMillis = 250,
    ) {
        var backgroundColor = if (currentTab === tab) MaterialTheme.colors.primary else Color.Transparent
        var contentColor = if (currentTab === tab) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
        if (LocalConfiguration.current.animations) {
            backgroundColor = animateColorAsState(backgroundColor).value
            contentColor = animateColorAsState(contentColor).value
        }

        Button(
            modifier = Modifier.size(50.dp),
            elevation = if (currentTab === tab) ButtonDefaults.elevation() else null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(12.dp),
            shape = UI.largeRoundedCornerShape,
            onClick = { onNavigate(tab) }
        ) {
            Icon(tab.icon, tab.title, Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun TabTooltip(tabName: String) {
    Surface(
        color = LocalPalette.current.surface1,
        modifier = Modifier.shadow(4.dp),
        shape = UI.largeRoundedCornerShape
    ) {
        Text(
            style = MaterialTheme.typography.body1,
            text = tabName,
            modifier = Modifier.padding(horizontal = UI.mediumPadding, vertical = UI.smallPadding),
        )
    }
}