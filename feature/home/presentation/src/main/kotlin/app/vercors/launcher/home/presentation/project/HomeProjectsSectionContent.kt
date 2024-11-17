package app.vercors.launcher.home.presentation.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
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

private val HomeProjectCardWidth = 330.dp

@Composable
fun HomeProjectsSectionContent(
    data: HomeSectionDataUi<HomeSectionItemUi.Project>,
    onProjectClick: (HomeSectionItemUi.Project) -> Unit,
    onProjectAction: (HomeSectionItemUi.Project) -> Unit
) {
    var count by rememberSaveable { mutableStateOf(0) }
    val localDensity = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).onGloballyPositioned {
            count = localDensity.run { (it.size.width.toDp() / HomeProjectCardWidth).toInt() }
        }
    ) {
        when (data) {
            is HomeSectionDataUi.Loaded -> {
                if (data.items.isNotEmpty()) {
                    for (project in data.items.take(count)) {
                        HomeProjectCard(
                            project = project,
                            onProjectClick = onProjectClick,
                            onProjectAction = onProjectAction
                        )
                    }
                } else {
                    Text("No projects found :(")
                }
            }

            is HomeSectionDataUi.Loading -> {
                repeat(count) {
                    HomeLoadingCard { HomeProjectCardBox(it) }
                }
            }
        }
    }
}
