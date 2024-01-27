package com.skyecodes.vercors.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.data.model.app.Instance
import com.skyecodes.vercors.ui.UI
import com.skyecodes.vercors.ui.instances.InstanceCardContent
import kotlin.math.roundToInt

@Composable
fun HomeInstancesSectionContent(instances: List<Instance>?) {
    var count by rememberSaveable { mutableStateOf(0) }
    val localDensity = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.fillMaxWidth().onGloballyPositioned {
            count = localDensity.run { ((it.size.width.toDp() - 80.dp) / 250.dp).roundToInt() }
        }
    ) {
        if (instances == null) {
            for (i in 0 until count) {
                FakeHomeCard { InstanceCardBox(it) }
            }
        } else if (instances.isNotEmpty()) {
            for (instance in instances.take(count)) {
                InstanceCardBox {
                    InstanceCardContent(instance)
                }
            }
            for (i in instances.size until count) {
                Spacer(Modifier.weight(1f))
            }
        } else {
            Text("No instances found :(")
        }
    }
}

@Composable
private fun RowScope.InstanceCardBox(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit = {}
) {
    Card(modifier = modifier.weight(1f).aspectRatio(0.8f), shape = shape) {
        content()
    }
}
