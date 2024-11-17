package app.vercors.launcher.home.presentation.instance

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.resources.*
import app.vercors.launcher.home.presentation.HomeInstanceStatusUi
import app.vercors.launcher.home.presentation.HomeSectionItemUi

@Composable
fun RowScope.HomeInstanceCard(
    instance: HomeSectionItemUi.Instance,
    onInstanceClick: () -> Unit,
    onInstanceAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeInstanceCardBox(
        onClick = onInstanceClick,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            HomeInstanceIcon(
                instance = instance,
                onAction = onInstanceAction,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = instance.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = appVectorResource { vercors },
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = instance.loaderAndGameVersion,
                            //style = MaterialTheme.typography.subtitle2
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val (statusIcon, statusText, statusColor) = when (instance.status) {
                            is HomeInstanceStatusUi.NotRunning -> Triple(
                                appVectorResource { play },
                                appStringResource(instance.status.lastPlayed) { last_played },
                                Color.Unspecified
                            )

                            HomeInstanceStatusUi.Running -> Triple(
                                appVectorResource { play },
                                appStringResource { running },
                                MaterialTheme.colorScheme.tertiary
                            )
                        }
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = statusColor
                        )
                        Text(
                            text = statusText,
                            color = statusColor,
                            //style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
            }
        }
    }
}