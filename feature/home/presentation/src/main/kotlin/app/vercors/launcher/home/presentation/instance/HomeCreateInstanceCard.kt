package app.vercors.launcher.home.presentation.instance

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.generated.resources.circle_plus
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.instance.generated.resources.new_instance
import app.vercors.launcher.instance.presentation.InstanceString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun RowScope.HomeCreateInstanceCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeInstanceCardBox(
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Icon(
                imageVector = vectorResource(CoreDrawable.circle_plus),
                contentDescription = null,
                modifier = Modifier.aspectRatio(1f).fillMaxSize().padding(10.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(InstanceString.new_instance),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}