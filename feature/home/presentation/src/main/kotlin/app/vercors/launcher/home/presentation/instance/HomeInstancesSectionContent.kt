package app.vercors.launcher.home.presentation.instance

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.vercors.launcher.home.presentation.HomeLoadingCard
import app.vercors.launcher.home.presentation.HomeSectionDataUi
import app.vercors.launcher.home.presentation.HomeSectionItemUi

val HomeInstanceCardWidth = 220.dp

@Composable
fun HomeInstancesSectionContent(
    data: HomeSectionDataUi<HomeSectionItemUi.Instance>,
    onInstanceClick: (HomeSectionItemUi.Instance) -> Unit,
    onInstanceAction: (HomeSectionItemUi.Instance) -> Unit,
    onCreateInstance: () -> Unit,
) {
    var count by rememberSaveable { mutableStateOf(0) }
    val density = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).onGloballyPositioned {
            count = with(density) { (it.size.width.toDp() / HomeInstanceCardWidth).toInt() }
        }
    ) {
        when (data) {
            is HomeSectionDataUi.Loaded -> {
                for (instance in data.items.take(count)) {
                    HomeInstanceCard(
                        instance = instance,
                        onInstanceClick = { onInstanceClick(instance) },
                        onInstanceAction = { onInstanceAction(instance) }
                    )
                }
                if (data.items.size < count) {
                    HomeCreateInstanceCard(
                        onClick = onCreateInstance
                    )
                }
                for (i in data.items.size + 1 until count) {
                    Spacer(Modifier.weight(1f))
                }
            }

            is HomeSectionDataUi.Loading -> {
                repeat(count) {
                    HomeLoadingCard { HomeInstanceCardBox(it) }
                }
            }
        }
    }
}