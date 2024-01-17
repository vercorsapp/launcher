package com.skyecodes.vercors.ui

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
import com.skyecodes.vercors.data.model.app.AppScene

@Composable
fun Menu(currentScene: AppScene, onNavigate: (AppScene) -> Unit) {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TabNavigationItem(AppScene.Home, currentScene, onNavigate)
        TabNavigationItem(AppScene.Instances, currentScene, onNavigate)
        TabNavigationItem(AppScene.Search, currentScene, onNavigate)
        Spacer(Modifier.weight(1f))
        TabNavigationItem(AppScene.Accounts, currentScene, onNavigate)
        TabNavigationItem(AppScene.Settings, currentScene, onNavigate)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabNavigationItem(scene: AppScene, currentScene: AppScene, onNavigate: (AppScene) -> Unit) {
    TooltipArea(
        tooltip = { TabTooltip(scene.title) },
        tooltipPlacement = TooltipPlacement.ComponentRect(
            alignment = Alignment.CenterEnd,
            offset = DpOffset(30.dp, -(25.dp))
        ),
        delayMillis = 250,
    ) {
        Button(
            modifier = Modifier.size(50.dp),
            elevation = if (currentScene === scene) ButtonDefaults.elevation() else null,
            colors = if (currentScene === scene) ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
            else ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onSurface),
            contentPadding = PaddingValues(12.dp),
            shape = UI.largeRoundedCornerShape,
            onClick = { onNavigate(scene) }
        ) {
            Icon(scene.icon, scene.title, Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun TabTooltip(sceneName: String) {
    Surface(
        color = LocalPalette.current.surface1,
        modifier = Modifier.shadow(4.dp),
        shape = UI.largeRoundedCornerShape
    ) {
        Text(
            style = MaterialTheme.typography.body1,
            text = sceneName,
            modifier = Modifier.padding(horizontal = UI.mediumPadding, vertical = UI.smallPadding),
        )
    }
}