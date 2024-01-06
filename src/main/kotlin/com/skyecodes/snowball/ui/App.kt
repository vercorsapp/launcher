package com.skyecodes.snowball.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowScope
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.skyecodes.snowball.APP_NAME
import com.skyecodes.snowball.APP_VERSION
import com.skyecodes.snowball.ui.home.HomeTab
import com.skyecodes.snowball.ui.instances.InstancesTab
import com.skyecodes.snowball.ui.search.SearchTab
import com.skyecodes.snowball.ui.settings.SettingsTab
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Times
import compose.icons.fontawesomeicons.solid.WindowMaximize
import compose.icons.fontawesomeicons.solid.WindowMinimize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WindowScope.App(onMinimize: () -> Unit, onMaximize: () -> Unit, onClose: () -> Unit) {
    MaterialTheme(colors = Theme.currentPalette.colors, typography = Typography(Theme.Font.family)) {
        TabNavigator(HomeTab) {
            Row {
                Surface(modifier = Modifier.fillMaxHeight().background(color = Theme.currentPalette.surface0)) {
                    Menu()
                }
                Column {
                    WindowDraggableArea(modifier = Modifier.combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                        onDoubleClick = onMaximize,
                        onLongClick = null
                    )) {
                        Surface(modifier = Modifier.height(40.dp).fillMaxWidth().background(color = Theme.currentPalette.surface0)) {
                            Toolbar(onMinimize, onMaximize, onClose)
                        }
                    }
                    Surface(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colors.background)) {
                        CurrentTab()
                    }
                }
            }
        }
    }
}

@Composable
private fun Menu() {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TabNavigationItem(HomeTab)
        TabNavigationItem(InstancesTab)
        TabNavigationItem(SearchTab)
        Spacer(Modifier.weight(1f))
        TabNavigationItem(SettingsTab)
    }
}

@Composable
private fun TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

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

@Composable
private fun Toolbar(onMinimize: () -> Unit, onMaximize: () -> Unit, onClose: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(APP_NAME)
            Text(
                text = "v$APP_VERSION",
                modifier = Modifier.padding(start = 5.dp),
                fontSize = 10.sp
            )
        }
        Spacer(Modifier.weight(1f))
        Row {
            WindowButton(FontAwesomeIcons.Solid.WindowMinimize, "Minimize Window", onMinimize)
            WindowButton(FontAwesomeIcons.Solid.WindowMaximize, "Maximize Window", onMaximize)
            WindowButton(FontAwesomeIcons.Solid.Times, "Close Window", onClose, true)
        }
    }
}

@Composable
private fun WindowButton(icon: ImageVector, name: String, onClick: () -> Unit, isRed: Boolean = false) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        Modifier.fillMaxHeight().width(40.dp).clickable(onClick = onClick).hoverable(interactionSource)
            .background(if (isRed && isHovered) MaterialTheme.colors.error else MaterialTheme.colors.surface)
    ) {
        Icon(icon, name, Modifier.align(Alignment.Center).size(16.dp))
    }
}