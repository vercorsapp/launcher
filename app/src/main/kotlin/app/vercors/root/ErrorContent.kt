package app.vercors.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.vercors.UI
import app.vercors.root.error.ErrorComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.fatalError
import vercors.app.generated.resources.fatalErrorDetails

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ErrorContent(component: ErrorComponent) {
    AppWindowContent {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize().padding(UI.largePadding)
        ) {
            Text(stringResource(Res.string.fatalError), style = MaterialTheme.typography.h6)
            Text(component.error.localizedMessage)
            Text(stringResource(Res.string.fatalErrorDetails))
        }
    }
}