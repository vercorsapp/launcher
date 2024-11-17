package app.vercors.launcher.core.presentation.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.generated.resources.close
import app.vercors.launcher.core.generated.resources.x
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.core.presentation.modifier.clickableWithoutRipple
import app.vercors.launcher.core.presentation.ui.AppIconTextButton
import app.vercors.launcher.core.presentation.ui.AppSectionTitle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AppDialogContainer(
    title: String,
    buttons: @Composable AppDialogButtonRowScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    Card(Modifier.clickableWithoutRipple()) {
        Column(
            modifier = Modifier.padding(20.dp).width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                AppSectionTitle(title)
                content()
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.End),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    buttons()
                }
            }
        }
    }
}

typealias AppDialogButtonRowScope = RowScope

@Composable
fun AppDialogButtonRowScope.AppDialogCloseButton(onClick: () -> Unit) {
    AppIconTextButton(
        onClick = onClick,
        icon = vectorResource(CoreDrawable.x),
        text = stringResource(CoreString.close),
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
    )
}