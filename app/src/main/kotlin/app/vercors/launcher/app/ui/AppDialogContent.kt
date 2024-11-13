package app.vercors.launcher.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.vercors.launcher.app.viewmodel.AppDialog
import app.vercors.launcher.app.viewmodel.AppUiIntent

@Composable
fun AppDialogContent(
    dialog: AppDialog,
    onIntent: (AppUiIntent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onIntent(AppUiIntent.CloseDialog) },
        contentAlignment = Alignment.Center
    ) {
        when (dialog) {
            AppDialog.AddAccount -> {

            }

            AppDialog.CreateInstance -> {

            }
        }
    }
}