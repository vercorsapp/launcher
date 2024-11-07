package app.vercors.launcher.core.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(text: String) = Text(
    text = text,
    style = MaterialTheme.typography.headlineMedium,
    modifier = Modifier.padding(20.dp)
)