package app.vercors.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.LocalPalette
import app.vercors.UI

@Composable
fun AppTooltip(text: String) {
    Card(
        backgroundColor = LocalPalette.current.surface1,
        elevation = 4.dp,
        shape = UI.largeRoundedCornerShape
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(horizontal = UI.mediumPadding, vertical = UI.smallPadding),
        )
    }
}