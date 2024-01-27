package com.skyecodes.vercors.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.data.model.app.AppTab
import com.skyecodes.vercors.ui.common.AppButton
import com.skyecodes.vercors.ui.common.appAnimateColorAsState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus

@Composable
fun Menu(currentTab: AppTab?, onNavigate: (AppTab) -> Unit, onCreateNewInstance: () -> Unit) {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TabNavigationItem(AppTab.Home, currentTab, onNavigate)
        TabNavigationItem(AppTab.Instances, currentTab, onNavigate)
        TabNavigationItem(AppTab.Search, currentTab, onNavigate)
        NavigationItem(LocalLocalization.current.createNewInstance, FeatherIcons.Plus, false, onCreateNewInstance)
        Spacer(Modifier.weight(1f))
        TabNavigationItem(AppTab.Accounts, currentTab, onNavigate)
        TabNavigationItem(AppTab.Settings, currentTab, onNavigate)
    }
}

@Composable
private fun TabNavigationItem(tab: AppTab, currentTab: AppTab?, onNavigate: (AppTab) -> Unit) {
    NavigationItem(tab.localizedTitle(LocalLocalization.current), tab.icon, tab == currentTab) { onNavigate(tab) }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NavigationItem(title: String, icon: ImageVector, active: Boolean, onClick: () -> Unit) {
    TooltipArea(
        tooltip = { TabTooltip(title) },
        tooltipPlacement = TooltipPlacement.ComponentRect(
            alignment = Alignment.CenterEnd,
            offset = DpOffset(30.dp, -(25.dp))
        ),
        delayMillis = 250,
    ) {
        val backgroundColor by appAnimateColorAsState(if (active) MaterialTheme.colors.primary else Color.Transparent)
        val contentColor by appAnimateColorAsState(if (active) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface)

        AppButton(
            modifier = Modifier.size(50.dp),
            elevation = if (active) ButtonDefaults.elevation() else null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(12.dp),
            shape = UI.largeRoundedCornerShape,
            onClick = onClick
        ) {
            Icon(icon, title, Modifier.fillMaxSize())
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