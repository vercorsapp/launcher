package com.skyecodes.snowball.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import com.skyecodes.snowball.APP_NAME
import com.skyecodes.snowball.APP_VERSION
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Times
import compose.icons.fontawesomeicons.solid.WindowMaximize
import compose.icons.fontawesomeicons.solid.WindowMinimize

@Composable
fun Toolbar(onMinimize: () -> Unit, onMaximize: () -> Unit, onClose: () -> Unit) {
    val tabNavigator = LocalTabNavigator.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(APP_NAME)
            Text("v$APP_VERSION", fontSize = 10.sp)
            Text("-", modifier = Modifier.padding(horizontal = 5.dp))
            Text(tabNavigator.current.options.title)
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