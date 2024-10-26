package app.vercors.launcher.home.presentation.ui

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.launcher.home.presentation.action.HomeAction
import app.vercors.launcher.home.presentation.model.HomeSectionItemUi
import app.vercors.launcher.home.presentation.model.HomeSectionUi
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeSection(
    section: HomeSectionUi,
    onAction: (HomeAction) -> Unit
) {
    val lazyListState = rememberLazyListState()

    Column {
        Text(
            text = stringResource(section.title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(20.dp)
        )
        LazyRow(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy((-20).dp)
        ) {
            items(section.items) { item ->
                when (item) {
                    is HomeSectionItemUi.Instance -> HomeInstanceCard(
                        instance = item,
                        /*onClick = { onInstanceClick(instance) },
                    onLaunch = { onInstanceLaunch(instance) },
                    onStop = { onInstanceStop(instance) },*/
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    is HomeSectionItemUi.Project -> TODO()
                }
            }
        }
        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(lazyListState),
            style = defaultScrollbarStyle().copy(
                unhoverColor = MaterialTheme.colorScheme.surface,
                hoverColor = MaterialTheme.colorScheme.surfaceBright
            ),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}