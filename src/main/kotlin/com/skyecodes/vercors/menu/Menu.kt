package com.skyecodes.vercors.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.UI
import com.skyecodes.vercors.accounts.Account
import com.skyecodes.vercors.accounts.AccountImage
import com.skyecodes.vercors.common.AppButton
import com.skyecodes.vercors.common.AppTooltip
import com.skyecodes.vercors.common.LocalLocalization
import com.skyecodes.vercors.common.appAnimateColorAsState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import compose.icons.feathericons.User

@Composable
fun Menu(
    currentTab: AppTab?,
    selectedAccount: Account?,
    onNavigate: (AppTab) -> Unit,
    onCreateNewInstance: () -> Unit,
    onOpenAccounts: () -> Unit
) {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TabNavigationItem(AppTab.Home, currentTab, onNavigate)
        TabNavigationItem(AppTab.Instances, currentTab, onNavigate)
        TabNavigationItem(AppTab.Search, currentTab, onNavigate)
        IconNavigationItem(LocalLocalization.current.createNewInstance, FeatherIcons.Plus, false, onCreateNewInstance)
        Spacer(Modifier.weight(1f))
        TabNavigationItem(AppTab.Settings, currentTab, onNavigate)
        if (selectedAccount != null) {
            ImageNavigationItem(LocalLocalization.current.accounts, selectedAccount, onOpenAccounts)
        } else {
            IconNavigationItem(LocalLocalization.current.accounts, FeatherIcons.User, false, onOpenAccounts)
        }
    }
}

@Composable
private fun TabNavigationItem(tab: AppTab, currentTab: AppTab?, onNavigate: (AppTab) -> Unit) {
    IconNavigationItem(tab.localizedTitle(LocalLocalization.current), tab.icon, tab == currentTab) { onNavigate(tab) }
}

@Composable
private fun IconNavigationItem(title: String, icon: ImageVector, active: Boolean, onClick: () -> Unit) {
    NavigationItem(title, active, onClick) {
        Icon(icon, title, Modifier.fillMaxSize())
    }
}

@Composable
private fun ImageNavigationItem(title: String, account: Account, onClick: () -> Unit) {
    NavigationItem(title, false, onClick, 0.dp) {
        AccountImage(account, Modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NavigationItem(
    title: String,
    active: Boolean,
    onClick: () -> Unit,
    contentPadding: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    TooltipArea(
        tooltip = { AppTooltip(title) },
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
            contentPadding = PaddingValues(contentPadding),
            shape = UI.largeRoundedCornerShape,
            onClick = onClick
        ) {
            content()
        }
    }
}
