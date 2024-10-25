package app.vercors.launcher.app.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.vercors.launcher.app.presentation.state.NavigationTab
import app.vercors.launcher.generated.resources.*
import org.jetbrains.compose.resources.vectorResource

@Composable
fun NavigationBar(
    currentTab: NavigationTab?,
    onTabSelection: (NavigationTab) -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            NavigationBarButton(
                selected = currentTab == NavigationTab.Home,
                icon = vectorResource(Res.drawable.house),
                onClick = { onTabSelection(NavigationTab.Home) }
            )
            NavigationBarButton(
                selected = currentTab == NavigationTab.Instances,
                icon = vectorResource(Res.drawable.hard_drive),
                onClick = { onTabSelection(NavigationTab.Instances) }
            )
            NavigationBarButton(
                selected = currentTab == NavigationTab.Projects,
                icon = vectorResource(Res.drawable.folder_cog),
                onClick = { onTabSelection(NavigationTab.Projects) }
            )
            Spacer(modifier = Modifier.weight(1f))
            NavigationBarButton(
                selected = currentTab == NavigationTab.Accounts,
                icon = vectorResource(Res.drawable.user),
                onClick = { onTabSelection(NavigationTab.Accounts) }
            )
            NavigationBarButton(
                selected = currentTab == NavigationTab.Settings,
                icon = vectorResource(Res.drawable.settings),
                onClick = { onTabSelection(NavigationTab.Settings) }
            )
        }
    }
}

@Composable
fun NavigationBarButton(
    selected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    val containerColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.background
    )
    val contentColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onBackground
    )

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = Modifier.size(64.dp),
        contentPadding = PaddingValues(12.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = icon,
            contentDescription = null
        )
    }
}