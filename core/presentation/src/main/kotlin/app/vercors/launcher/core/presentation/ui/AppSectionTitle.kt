package app.vercors.launcher.core.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AppSectionTitle(text: String) = Text(
    text = text,
    style = MaterialTheme.typography.headlineMedium
)