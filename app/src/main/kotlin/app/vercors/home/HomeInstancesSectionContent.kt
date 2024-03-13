package app.vercors.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.vercors.UI
import app.vercors.instance.InstanceCardContent
import app.vercors.instance.InstanceData
import kotlin.math.roundToInt

@Composable
fun HomeInstancesSectionContent(
    instances: List<InstanceData>?,
    onInstanceClick: (InstanceData) -> Unit,
    onInstanceLaunchClick: (InstanceData) -> Unit,
) {
    var count by rememberSaveable { mutableStateOf(0) }
    val localDensity = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.fillMaxWidth().onGloballyPositioned {
            count = localDensity.run { ((it.size.width.toDp() - 80.dp) / 200.dp).roundToInt() }
        }
    ) {
        if (instances?.isNotEmpty() == true) {
            for (instance in instances.take(count)) {
                InstanceCardContent(
                    instance = instance,
                    onInstanceClick = onInstanceClick,
                    onInstanceLaunchClick = onInstanceLaunchClick,
                    modifier = Modifier.weight(1f)
                )
            }
            for (i in instances.size until count) {
                Spacer(Modifier.weight(1f))
            }
        } else {
            Text("No instances found :(")
        }
    }
}
