package com.skyecodes.vercors.dialog.login

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skyecodes.vercors.UI
import com.skyecodes.vercors.accounts.AccountImage
import com.skyecodes.vercors.common.*
import compose.icons.FeatherIcons
import compose.icons.feathericons.*

@Composable
fun LoginDialogContent(component: LoginDialogComponent) {
    val locale = LocalLocalization.current
    val clipboardManager = LocalClipboardManager.current
    val uiState by component.uiState.collectAsState()
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.progress,
        animationSpec = tween(500)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.padding(UI.largePadding).appAnimateContentSize()
    ) {
        Text(locale.logIn, style = MaterialTheme.typography.h5)
        if (!uiState.isSuccess) {
            Text(locale.logInInfo, style = MaterialTheme.typography.h6)
        }

        if (uiState.isWaitingLogin) {
            Text(locale.logInWaitingForUser)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconTextButton(
                    enabled = component.canOpenInBrowser && uiState.hasUrl,
                    onClick = { component.openInBrowser(uiState.url!!) },
                    imageVector = FeatherIcons.ExternalLink,
                    text = locale.openInBrowser
                )
                IconTextButton(
                    enabled = uiState.hasUrl,
                    onClick = { clipboardManager.setText(AnnotatedString(uiState.url!!)) },
                    imageVector = FeatherIcons.Copy,
                    text = locale.copyUrl,
                    colors = ButtonDefaults.buttonColors(backgroundColor = LocalPalette.current.surface2)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(UI.mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.error != null) {
                    Icon(FeatherIcons.XCircle, null, Modifier.size(UI.mediumIconSize), MaterialTheme.colors.error)
                } else if (uiState.isSuccess) {
                    Icon(FeatherIcons.Check, null, Modifier.size(UI.mediumIconSize), LocalPalette.current.green)
                }
                Text(
                    text = if (uiState.isSuccess) locale.accountSuccess
                    else if (uiState.isWaitingLogin) locale.awaitingAuth
                    else "${locale.authenticating}...",
                    color = if (uiState.error != null) MaterialTheme.colors.error
                    else if (uiState.isSuccess) LocalPalette.current.green
                    else MaterialTheme.colors.onSurface,
                    lineHeight = UI.normalLineHeight,
                    fontWeight = if (uiState.isSuccess || uiState.error != null) FontWeight.SemiBold else null
                )
            }
            if (uiState.progress < 0 && uiState.error == null) LinearProgressIndicator()
            else if (!uiState.isSuccess) LinearProgressIndicator(animatedProgress)
        }

        if (uiState.error != null) {
            Text(uiState.error!!.message ?: locale.errorOccurred, color = MaterialTheme.colors.error)
        } else if (uiState.account != null) {
            val account = uiState.account!!
            Row(
                modifier = Modifier.height(40.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(UI.largePadding, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(Modifier.fillMaxHeight()) {
                    AccountImage(account, Modifier.fillMaxHeight())
                }
                Text(account.name, style = MaterialTheme.typography.h6)
            }
        }

        if (uiState.canClose) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AppTextButton(component::close) {
                    Icon(
                        imageVector = FeatherIcons.X,
                        contentDescription = null,
                        modifier = Modifier.size(UI.mediumIconSize)
                    )
                    Text(
                        text = LocalLocalization.current.close,
                        modifier = Modifier.padding(start = UI.mediumPadding)
                    )
                }
            }
        }
    }
}