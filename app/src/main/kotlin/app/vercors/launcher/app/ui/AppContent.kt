package app.vercors.launcher.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.vercors.launcher.app.navigation.AppDestination
import app.vercors.launcher.app.navigation.TopLevelDestination
import app.vercors.launcher.app.util.screenName
import app.vercors.launcher.app.util.screenType
import app.vercors.launcher.app.viewmodel.AppUiIntent
import app.vercors.launcher.app.viewmodel.GeneralConfigState
import app.vercors.launcher.core.config.model.TabConfig
import app.vercors.launcher.core.generated.resources.app_title
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.core.presentation.ui.PopupAlignment
import app.vercors.launcher.core.presentation.ui.handPointer
import app.vercors.launcher.core.presentation.ui.rememberPopupPositionProvider
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

private val logger = KotlinLogging.logger {}

@Composable
fun WindowScope.AppContent(
    navController: NavHostController,
    windowState: WindowState,
    generalConfig: GeneralConfigState.Loaded,
    onClose: () -> Unit,
    onIntent: (AppUiIntent) -> Unit,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val screenType = currentBackStackEntry?.destination?.screenType
    val screenName = screenType.screenName ?: CoreString.app_title
    val currentBackStack by navController.currentBackStack.collectAsState()
    val canGoBack by remember { derivedStateOf { currentBackStack.size > 2 } }
    val navItemColors = NavigationSuiteDefaults.itemColors(
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        AppMenuBar(
            screenName = stringResource(screenName),
            hasWindowControls = !generalConfig.decorated,
            canGoBack = canGoBack,
            isMaximized = windowState.placement == WindowPlacement.Maximized,
            onAction = {
                when (it) {
                    MenuBarAction.Close -> onClose()
                    MenuBarAction.Maximize -> windowState.placement =
                        if (windowState.placement == WindowPlacement.Maximized) WindowPlacement.Floating else WindowPlacement.Maximized

                    MenuBarAction.Minimize -> windowState.isMinimized = true
                    MenuBarAction.Back -> if (canGoBack) navController.popBackStack()
                }
            },
        )
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                TopLevelDestination.entries.forEach { topLevelRoute ->
                    if (topLevelRoute == TopLevelDestination.Accounts) {
                        item(
                            modifier = Modifier.weight(1f),
                            selected = false,
                            enabled = false,
                            icon = {},
                            onClick = {}
                        )
                    }
                    item(
                        modifier = Modifier.padding(vertical = 16.dp).size(32.dp).handPointer(),
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(route = topLevelRoute.route::class) } == true,
                        icon = {
                            TooltipBox(
                                state = rememberTooltipState(),
                                positionProvider = rememberPopupPositionProvider(alignment = PopupAlignment.CenterRight),
                                tooltip = {
                                    PlainTooltip(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.onSurface,
                                    ) {
                                        Text(
                                            text = stringResource(topLevelRoute.title),
                                            style = MaterialTheme.typography.bodyLarge,
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    imageVector = vectorResource(topLevelRoute.icon),
                                    contentDescription = stringResource(topLevelRoute.title)
                                )
                            }
                        },
                        colors = navItemColors,
                        onClick = {
                            logger.info { "Navigating to $topLevelRoute tab" }
                            navController.navigate(route = topLevelRoute.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            },
            layoutType = NavigationSuiteType.NavigationRail,
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationRailContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            ContentBackground(gradient = generalConfig.gradient) {
                AppNavHost(
                    navController = navController,
                    startDestination = generalConfig.defaultTab.toDestination(),
                    onIntent = onIntent
                )
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