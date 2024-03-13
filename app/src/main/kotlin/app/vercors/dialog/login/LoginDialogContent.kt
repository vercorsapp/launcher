package app.vercors.dialog.login

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
import app.vercors.LocalPalette
import app.vercors.UI
import app.vercors.account.AccountImage
import app.vercors.common.AppTextButton
import app.vercors.common.IconTextButton
import app.vercors.common.appAnimateContentSize
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.*

@Composable
fun LoginDialogContent(component: LoginDialogComponent) {
    val clipboardManager = LocalClipboardManager.current
    val uiState by component.uiState.collectAsState()
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.progress,
        animationSpec = tween(500)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.padding(UI.largePadding).width(500.dp).appAnimateContentSize()
    ) {
        Text(stringResource(Res.string.logIn), style = MaterialTheme.typography.h5)
        if (!uiState.isSuccess) {
            Text(stringResource(Res.string.logInInfo), style = MaterialTheme.typography.h6)
        }

        if (uiState.isWaitingLogin) {
            Text(stringResource(Res.string.logInWaitingForUser))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconTextButton(
                    enabled = component.canOpenInBrowser && uiState.hasUrl,
                    onClick = { component.openInBrowser(uiState.url!!) },
                    imageVector = FeatherIcons.ExternalLink,
                    text = stringResource(Res.string.openInBrowser)
                )
                IconTextButton(
                    enabled = uiState.hasUrl,
                    onClick = { clipboardManager.setText(AnnotatedString(uiState.url!!)) },
                    imageVector = FeatherIcons.Copy,
                    text = stringResource(Res.string.copyUrl),
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
                LoginStatusContent(uiState)
            }
            if (uiState.progress < 0 && uiState.error == null) LinearProgressIndicator()
            else if (!uiState.isSuccess) LinearProgressIndicator(animatedProgress)
        }

        if (uiState.error != null) {
            Text(
                uiState.error!!.message ?: stringResource(Res.string.errorOccurred),
                color = MaterialTheme.colors.error
            )
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
                        text = stringResource(Res.string.close),
                        modifier = Modifier.padding(start = UI.mediumPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginStatusContent(uiState: LoginDialogUiState) {
    if (uiState.error != null) {
        Icon(FeatherIcons.XCircle, null, Modifier.size(UI.mediumIconSize), MaterialTheme.colors.error)
    } else if (uiState.isSuccess) {
        Icon(FeatherIcons.Check, null, Modifier.size(UI.mediumIconSize), LocalPalette.current.green)
    }
    Text(
        text = if (uiState.isSuccess) stringResource(Res.string.accountSuccess)
        else if (uiState.isWaitingLogin) stringResource(Res.string.awaitingAuth)
        else "${stringResource(Res.string.authenticating)}...",
        color = if (uiState.error != null) MaterialTheme.colors.error
        else if (uiState.isSuccess) LocalPalette.current.green
        else MaterialTheme.colors.onSurface,
        lineHeight = UI.normalLineHeight,
        fontWeight = if (uiState.isSuccess || uiState.error != null) FontWeight.SemiBold else null
    )
}
