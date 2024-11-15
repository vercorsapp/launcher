package app.vercors.launcher.home.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.presentation.ui.AppSectionTitle
import app.vercors.launcher.home.presentation.ui.instance.HomeInstancesSectionContent
import app.vercors.launcher.home.presentation.ui.project.HomeProjectsSectionContent
import app.vercors.launcher.home.presentation.viewmodel.HomeSectionUi
import app.vercors.launcher.home.presentation.viewmodel.HomeUiIntent
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeSection(
    section: HomeSectionUi,
    onIntent: (HomeUiIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxWidth().heightIn(max = 600.dp)
    ) {
        AppSectionTitle(text = stringResource(section.title))
        when (section) {
            is HomeSectionUi.Instances -> HomeInstancesSectionContent(
                data = section.data,
                onInstanceClick = { onIntent(HomeUiIntent.ShowInstance(it.id)) },
                onInstanceAction = { onIntent(HomeUiIntent.LaunchOrStopInstance(it.id)) },
                onCreateInstance = { onIntent(HomeUiIntent.CreateInstance) }
            )

            is HomeSectionUi.Projects -> HomeProjectsSectionContent(
                data = section.data,
                onProjectClick = { onIntent(HomeUiIntent.ShowProject(it.id)) },
                onProjectAction = { onIntent(HomeUiIntent.InstallProject(it.id)) }
            )
        }
    }
}