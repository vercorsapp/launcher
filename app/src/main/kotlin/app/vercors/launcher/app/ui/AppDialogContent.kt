package app.vercors.launcher.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.vercors.launcher.app.viewmodel.AppDialog
import app.vercors.launcher.app.viewmodel.AppUiIntent
import app.vercors.launcher.core.presentation.modifier.clickableWithoutRipple
import app.vercors.launcher.core.presentation.ui.thenIf
import app.vercors.launcher.instance.presentation.create.CreateInstanceDialog

@Composable
fun AppDialogContent(
    dialog: AppDialog?,
    onIntent: (AppUiIntent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .thenIf(dialog != null) { clickableWithoutRipple { onIntent(AppUiIntent.CloseDialog) } },
        contentAlignment = Alignment.Center
    ) {
        dialog?.let {
            when (it) {
                AppDialog.AddAccount -> {

                }

                AppDialog.CreateInstance -> {
                    CreateInstanceDialog(
                        onClose = { onIntent(AppUiIntent.CloseDialog) },
                    )
                }
            }
        }
    }
}