package app.vercors.launcher.app.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.vercors.launcher.app.navigation.AppDestination
import app.vercors.launcher.app.presentation.action.MenuBarAction
import app.vercors.launcher.app.presentation.state.GeneralConfigState
import app.vercors.launcher.app.presentation.util.currentTab
import app.vercors.launcher.app.presentation.util.defaultDestination
import app.vercors.launcher.app.presentation.util.screenName
import app.vercors.launcher.app.presentation.util.screenType
import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.generated.resources.app_title
import app.vercors.launcher.core.presentation.CoreString
import org.jetbrains.compose.resources.stringResource

@Composable
fun WindowScope.AppContent(
    navController: NavHostController,
    windowState: WindowState,
    generalConfig: GeneralConfigState.Loaded,
    onClose: () -> Unit,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val screenType by remember { derivedStateOf { currentBackStackEntry?.destination?.screenType } }
    val screenName by remember { derivedStateOf { screenType.screenName ?: CoreString.app_title } }
    val currentTab by remember { derivedStateOf { screenType.currentTab } }

    Column(modifier = Modifier.fillMaxSize()) {
        MenuBar(
            screenName = stringResource(screenName),
            hasWindowControls = !generalConfig.decorated,
            isMaximized = windowState.placement == WindowPlacement.Maximized,
            onAction = {
                when (it) {
                    MenuBarAction.Close -> onClose()
                    MenuBarAction.Maximize -> windowState.placement =
                        if (windowState.placement == WindowPlacement.Maximized) WindowPlacement.Floating else WindowPlacement.Maximized

                    MenuBarAction.Minimize -> windowState.isMinimized = true
                }
            },
        )
        Row(modifier = Modifier.weight(1f)) {
            NavigationBar(
                currentTab = currentTab,
                onTabSelection = {
                    // TODO rework tab navigation (nested graphs?)
                    navController.navigate(it.defaultDestination) {
                        launchSingleTop = true
                    }
                }
            )
            Box(modifier = Modifier.padding(bottom = 5.dp, end = 5.dp)) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavHost(
                        navController = navController,
                        startDestination = generalConfig.defaultTab.toDestination()
                    )
                }
            }
        }
    }
}

@Stable
private fun TabConfig.toDestination(): AppDestination = when (this) {
    TabConfig.Home -> AppDestination.Home
    TabConfig.Instances -> AppDestination.InstanceList
    TabConfig.Projects -> AppDestination.ProjectList
    TabConfig.Accounts -> AppDestination.Accounts
    TabConfig.Settings -> AppDestination.Settings
}